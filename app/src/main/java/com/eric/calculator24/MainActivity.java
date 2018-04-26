package com.eric.calculator24;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private int[] array = new int[4];

    private Button btn_search;
    private Button btn_clear;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private TextView textView;


    private boolean hasAdd = false;
    private String myString;
    private ArrayList<String> allList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_search = findViewById(R.id.btn_calculate);
        btn_clear = findViewById(R.id.btn_clear);
        editText1 = findViewById(R.id.num1);
        editText2 = findViewById(R.id.num2);
        editText3 = findViewById(R.id.num3);
        editText4 = findViewById(R.id.num4);
        textView  = findViewById(R.id.txt_content);

        editText1.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        editText2.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        editText3.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        editText4.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        btn_search.setOnClickListener(onClickListener);
        btn_clear.setOnClickListener(onClickListener);

        setEditTextInhibitInputSpace();

        allList = new ArrayList<>();

    }



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_calculate:
                    try {
                        int a = Integer.parseInt(editText1.getText().toString());
                        int b = Integer.parseInt(editText2.getText().toString());
                        int c = Integer.parseInt(editText3.getText().toString());
                        int d = Integer.parseInt(editText4.getText().toString());

                        Set<String> set = caculate(new int[]{a, b, c, d}, 24);
                        printlnResultSet(set);
                    } catch (Exception e) {
                        textView.setText("计算不出来了");
                    }
                    break;
                case R.id.btn_clear:
                    editText1.setText("");
                    editText2.setText("");
                    editText3.setText("");
                    editText4.setText("");
                    editText1.setFocusable(true);
                    editText1.setFocusableInTouchMode(true);
                    editText1.requestFocus();
                    textView.setText("");
                    allList.clear();
                    break;
                default:
                    break;
            }

        }

    };

    public void setEditTextInhibitInputSpace(){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(" "))
                    return "";
                else return null;
            }
        };
        editText1.setFilters(new InputFilter[]{filter});
        editText2.setFilters(new InputFilter[]{filter});
        editText3.setFilters(new InputFilter[]{filter});
        editText4.setFilters(new InputFilter[]{filter});
    }

    private void printlnResultSet(Collection<String> resultSet) {
        for (String str : resultSet) {
            System.out.println("str:" + str);
            if ((str.charAt(2) == "+".charAt(0) || str.charAt(2) == "-".charAt(0)) && (str.charAt(6) == "*".charAt(0) || str.charAt(6) == "/".charAt(0)) ){
                StringBuilder stringBuilder = new StringBuilder(str);
                stringBuilder.insert(0,"(");
                stringBuilder.insert(6,")");
                hasAdd = true;
                myString = stringBuilder.toString();
                System.out.println(stringBuilder.toString());

                allList.add(stringBuilder.toString());

            }
            if (hasAdd){

                if ((myString.charAt(8) == "+".charAt(0) || myString.charAt(8) == "-".charAt(0)) && (myString.charAt(12) == "*".charAt(0) || myString.charAt(12) == "/".charAt(0))){
                    StringBuilder stringBuilder = new StringBuilder(str);
                    stringBuilder.insert(0,"(");
                    stringBuilder.insert(11,")");
                    System.out.println(stringBuilder.toString());
                    allList.add(stringBuilder.toString());
                }
                hasAdd = false;
            }else {
                if ((str.charAt(6) == "+".charAt(0) || str.charAt(6) == "-".charAt(0)) && (str.charAt(10) == "*".charAt(0) || str.charAt(10) == "/".charAt(0))){
                    StringBuilder stringBuilder = new StringBuilder(str);
                    stringBuilder.insert(0,"(");
                    stringBuilder.insert(11,")");
                    System.out.println(stringBuilder.toString());
                    allList.add(stringBuilder.toString());
                }else {
                    allList.add(str);
                }
            }
        }
        String output = "";
        for (int i = 0; i < allList.size(); i ++ ){
            output = output + allList.get(i) + "\n";
        }
        textView.setText(output);
    }

    /**
     * 得到给定整形数组的全排列情况
     *
     * @param numbers
     *            给定的整形数组
     * @return 全排列数组
     */
    private  int[][] arrangeAllNumbers(int[] numbers) {
        List<int[]> list = new ArrayList<int[]>();
        allSort(numbers, 0, numbers.length - 1, list);
        int[][] resultSet = new int[list.size()][list.get(0).length];
        resultSet = list.toArray(resultSet);
        return resultSet;
    }

    /**
     * 得到给定的操作中出现的所有操作符排列情况
     *
     * @param operators
     *            出现的操作符数组
     * @param number
     *            每组操作符的数量
     * @return 所有操作符排列数组
     */
    private char[][] arrangeAllOperators(char[] operators, int number) {
        int setSize = (int) Math.pow(operators.length, number);
        int index = 0;
        char[][] resultSet = new char[setSize][number];
        for (int i = 0; i < operators.length; i++) {
            for (int j = 0; j < operators.length; j++) {
                for (int k = 0; k < operators.length; k++) {
                    resultSet[index][0] = operators[i];
                    resultSet[index][1] = operators[j];
                    resultSet[index][2] = operators[k];
                    index++;
                }
            }
        }
        return resultSet;
    }

    /**
     * 根据给定的一组整数，通过加减乘除运算，得到想要的结果，如果可以得到结果，则返回所有可能的结果的运算形式。
     * 返回的运算形式，均按从左到右的顺序计算，并不是遵循四则运算法则，比如：<br>
     * 输出的结果形式为：<br>
     * 1 * 8 - 6 * 12 = 24 <br>
     * 表示的运算顺序是：<br>
     * 1:1 * 8 = 8,<br>
     * 2:8 - 6 = 2,<br>
     * 3:2 * 12 = 24<br>
     * 而不是按照四则运算法则计算：<br>
     * 1:1 * 8 = 8,<br>
     * 2:6 * 12 = 72,<br>
     * 3:8 * 72 = 576<br>
     *
     *
     * @param numbers
     *            给定进行运算的一组整数，4个数为一组
     * @param targetNumber
     *            想要得到的结果
     * @return 所有可能得到想要的结果的所有运算形式的字符串形式集合
     * @throws Exception
     *             如果不能得到想要的结果，则抛出该异常，表明根据指定的一组数字通过一系列的加减乘除不能得到想要的结果
     */
    public Set<String> caculate(int[] numbers, int targetNumber)
            throws Exception {
        Set<String> resultSet = new HashSet<String>();// 这里用Set而不是用List，主要是因为当给定的一组数字中如果有重复数字的话，同一结果会被出现多次，如果用List存放的话，会将重复的结果都存放起来，而Set会自动消除重复值
        char[][] operatorsArrangement = arrangeAllOperators(new char[] { '+',
                '-', '*', '/' }, 3);
        int[][] numbersArrangement = arrangeAllNumbers(numbers);
        for (int[] nums : numbersArrangement)
            for (char[] operators : operatorsArrangement) {
                int result = 0;
                try {
                    result = caculate(nums, operators);
                } catch (Exception e) {// 出现非精确计算
                    continue;
                }
                if (result == targetNumber)
                    resultSet.add(buildString(nums, operators, targetNumber));// 如果计算后的结果等于想要的结果，就存放到集合中
            }
        if (resultSet.isEmpty())

            throw new Exception("给定的数字：" + Arrays.toString(numbers)
                    + "不能通过加减乘除运算得到结果：" + targetNumber);
        return resultSet;
    }

    /**
     * 将一组整型数字以给定的操作符按顺序拼接为一个完整的表达式字符串
     *
     * @param nums
     *            一组整型数字
     * @param operators
     *            一组操作符
     * @param target
     *            目标值
     * @return 拼接好的表达式字符串
     */
    private String buildString(int[] nums, char[] operators, int target) {
        String str = String.valueOf(nums[0]);
        for (int i = 0; i < operators.length; i++) {
            str = str + ' ' + operators[i] + ' ' + nums[i + 1];
        }
        str = str + " = " + target;
        return str;
    }

    /**
     * 将给定的一组数字以给定的操作符按顺序进行运算，如：int result = caculate(new int[]{3,4,5,8}, new
     * char[]{'+','-','*'});
     *
     * @param nums
     *            一组数字
     * @param operators
     *            一组运算符，数量为数字的个数减1
     * @return 最后的计算结果
     * @throws Exception
     *             当计算结果不精确时，抛出该异常，主要是针对除法运算，例如18 / 8 = 2，诸如这样不精确计算将抛出该异常
     */
    private int caculate(int[] nums, char[] operators) throws Exception {
        int result = 0;
        for (int i = 0; i < operators.length; i++) {
            if (i == 0) {
                result = caculate(nums[i], nums[i + 1], operators[i]);
            } else {
                result = caculate(result, nums[i + 1], operators[i]);
            }
        }
        return result;
    }

    /**
     * 根据指定操作符将两个给定的数字进行计算
     *
     * @param num1
     *            数字1
     * @param num2
     *            数字2
     * @param operator
     *            操作符，只能从“+、-、*、/”4个操作符中取值
     * @return 计算结果
     * @throws Exception
     *             当计算结果不精确时，抛出该异常，主要是针对除法运算，例如18 / 8 = 2，诸如这样不精确计算将抛出该异常
     */
    private int caculate(int num1, int num2, char operator)
            throws Exception {
        double result = 0;
        switch (operator) {// 根据操作符做相应的计算操作
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                result = (double) num1 / (double) num2;
                break;
        }
        if (!check(result))
            throw new Exception("不精确的计算数字");
        return (int) result;
    }

    /**
     * 检查指定的浮点数是否可以直接转换为整型数字而不损失精度
     *
     * @param result
     *            要检查的浮点数
     * @return 如果可以进行无损转换，返回true，否则返回false
     */
    private boolean check(double result) {
        String str = String.valueOf(result);
        int pointIndex = str.indexOf(".");// 小数点的下标值
        String fraction = str.substring(pointIndex + 1);
        return fraction.equals("0") ? true : false;// 通过判断小数点后是否只有一个0来确定是否可以无损转换为整型数值
    }

    /**
     * 对传入的整型数组buf进行全排列
     *
     * @param buf
     *            要进行全排列的整型数组
     * @param start
     *            开始的下标值
     * @param end
     *            结束下标值
     * @param list
     *            保存最后全排列结果的集合
     */
    private void allSort(int[] buf, int start, int end, List<int[]> list) {
        if (start == end) {// 当只要求对数组中一个字母进行全排列时，只要就按该数组输出即可
            int[] a = new int[buf.length];
            System.arraycopy(buf, 0, a, 0, a.length);
            list.add(a);
        } else {// 多个字母全排列
            for (int i = start; i <= end; i++) {
                int temp = buf[start];// 交换数组第一个元素与后续的元素
                buf[start] = buf[i];
                buf[i] = temp;
                allSort(buf, start + 1, end, list);// 后续元素递归全排列
                temp = buf[start];// 将交换后的数组还原
                buf[start] = buf[i];
                buf[i] = temp;
            }
        }
    }
}


