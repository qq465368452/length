import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * ���򻮷�Ϊ����ִ�У���һ���ǽ���ת�����ڶ����Ǳ��ʽ��ֵ
 * @author SunLijun
 *
 */
public class EvaluateExpression {

	
	private final static String MY_EMAIL = "qq465368452@126.com";
	private final static String INPUT_PATH = "input.txt";
	private final static String OUTPUT_PATH = "output.txt";
	// �������б�
	private static List<Double> resultList = new ArrayList<Double>();
	// ����ת����Ӧmap
	private static Map<String,String> convMap = new HashMap<String,String>();
	// ��������ʽ
	private static List<String> expressList = new ArrayList<String>();
	// �����
    private static List<Character> ops = Arrays.asList('+', '-', '*', '/', '(',  
            ')', '[', ']', '{', '}', '#');  
  
    // ------------------ ����������Ҳ��������ȼ���ϵ�� -------------------  
    // |------'+', '-', '*', '/', '(', ')', '[', ']', '{', '}', '#'--|  
    // |-------------------------------------------------------------|  
    private static char[][] prioTable = new char[][] {  
            { '>', '>', '<', '<', '<', '>', '<', '>', '<', '>', '>' }, // +  
            { '>', '>', '<', '<', '<', '>', '<', '>', '<', '>', '>' }, // -  
            { '>', '>', '>', '>', '<', '>', '<', '>', '<', '>', '>' }, // *  
            { '>', '>', '>', '>', '<', '>', '<', '>', '<', '>', '>' }, // /  
            { '<', '<', '<', '<', '<', '=', ' ', ' ', ' ', ' ', ' ' }, // (  
            { '>', '>', '>', '>', ' ', '>', ' ', ' ', ' ', ' ', '>' }, // )  
            { '<', '<', '<', '<', '<', ' ', '<', '=', ' ', ' ', ' ' }, // [  
            { '>', '>', '>', '>', ' ', ' ', '=', ' ', ' ', '>', '>' }, // ]  
            { '<', '<', '<', '<', '<', ' ', '<', ' ', '<', '=', ' ' }, // {  
            { '>', '>', '>', '>', ' ', ' ', ' ', ' ', '=', '>', '>' }, // }  
            { '<', '<', '<', '<', '<', ' ', '<', ' ', '<', ' ', '=' } }; // #  
  
    public static char precede(char l, char r) {  
        return prioTable[ops.indexOf(l)][ops.indexOf(r)];  
    }  
  
    public static double operate(double l, double r, char op) {  
        switch (op) {  
        case '+':  
            return l + r;  
        case '-':  
            return l - r;  
        case '*':  
            return l * r;  
        case '/':  
            return l / r;  
        }  
        return 0;  
    }  
  
    public static double evaluate(String expression) {  
        // process expression  
        expression = expression.replaceAll("\\s+", "");  
        expression += '#';  
  
        Stack<Double> operands = new Stack<Double>();  
        Stack<Character> operators = new Stack<Character>();  
  
        operators.push('#');  
        int index = 0;  
        while (expression.charAt(index) != '#' || operators.peek() != '#') {  
            if (!ops.contains(expression.charAt(index))) {  
                int end = index;  
                for (; end < expression.length(); ++end) {  
                    if (ops.contains(expression.charAt(end + 1)))  
                        break;  
                }  
                operands.push(Double.parseDouble(expression.substring(index,  
                        end + 1)));  
                index = end + 1;  
            } else {  
                switch (precede(operators.peek(), expression.charAt(index))) {  
                case '<':  
                    operators.push(expression.charAt(index++));  
                    break;  
                case '=':  
                    operators.pop();  
                    index++;  
                    break;  
                case '>':  
                    char op = operators.pop();  
                    double r = operands.pop();  
                    double l = operands.pop();  
                    operands.push(operate(l, r, op));  
                    break;  
                default:  
                    throw new IllegalArgumentException(String.format(  
                            "�������쳣:%c, %c", operators.peek(),  
                            expression.charAt(index)));  
                }  
            }  
        }  
        return operands.pop();  
    } 
    
	/**
     * ����Ϊ��λ��ȡ�ļ�
     */
	public static void readFile(String filepath) {
		File file = new File(filepath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
            	// ����
            	if(tempString.trim().equals("")) {
            		// ����
            	}
                // ת��
            	else if(-1 != tempString.indexOf('=')) {
            		String arr[] = tempString.split("=");
            		String key = arr[0].trim().substring(1,arr[0].trim().length()).trim();
            		String value = arr[1].trim().substring(0,arr[1].trim().length()-1).trim();
            		if (key.equals("mile")) {
            			convMap.put(key, value);
            			convMap.put("miles", value);
            		}
            		else if(key.equals("yard")) {
            			convMap.put(key, value);
            			convMap.put("yards", value);
            		}
            		else if(key.equals("inch")) {
            			convMap.put(key, value);
            			convMap.put("inches", value);
            		}
            		else if(key.equals("foot")) {
            			convMap.put(key, value);
            			convMap.put("feet", value);
            		}
            		else if(key.equals("fath")) {
            			convMap.put(key, value);
            			convMap.put("faths", value);
            		}
            		else if(key.equals("furlong")) {
            			convMap.put(key, value);
            			convMap.put("furlongs", value);
            		}
            	}
                // ���ʽ
            	else {
            		// ������ʽ
            		tempString = tempString.replace("miles", "*"+convMap.get("miles"));
            		tempString = tempString.replace("yards", "*"+convMap.get("yards"));
            		tempString = tempString.replace("inches", "*"+convMap.get("inches"));
            		tempString = tempString.replace("furlongs", "*"+convMap.get("furlong"));
            		tempString = tempString.replace("faths", "*"+convMap.get("faths"));
            		tempString = tempString.replace("feet", "*"+convMap.get("feet"));
            		// ������ʽ
            		for(String opt : convMap.keySet())
            			tempString = tempString.replace(opt, "*"+convMap.get(opt));
            		expressList.add(tempString);
            	}
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                }
            }
        }
	}
	
    /**
     * ׷���ļ���ʹ��FileWriter
     */
	public static void writeFile(String filepath, String content) {
        try {
            //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
            FileWriter writer = new FileWriter(filepath, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	
	public static void main(String[] args) 	{

		readFile(INPUT_PATH);
		for(String express : expressList) {
			Double result = evaluate(express);
			resultList.add(result);
		}
		// ע������
		writeFile(OUTPUT_PATH, MY_EMAIL + "\r\n");
		// ����
		writeFile(OUTPUT_PATH, "\r\n");
		// ������
		for(Double result : resultList) {
			writeFile(OUTPUT_PATH, String.format("%.2f", result) + " m\r\n");
		}
	}
}