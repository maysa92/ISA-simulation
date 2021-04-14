
package isasim;

import java.io.*;
import java.util.*;

/**
 * General Description
 * Machine type: 10 bit decimal
 * This is an ISA Generator consisted of 5 functions
 * @author Sheryl
 */
public class IsaSim {

    //global var
    public static String ISA[] = new String[100];//array for the content
    public static int cnt = 0;
    public static String var_name[] = new String[30];//array for the variable name
    public static int vn1 = 0;
    public static float var_val[][] = new float[30][30];//array for the variable value
    public static int array_val[] = new int[100];//array for array values
    public static int vv1 = 0;
    public static float data[] = new float[20];//array for data in float
    public static int d1 = 0;
    public static String memory[] = new String[30];
    public static int m3 = 0;
    public static int loop_times = 0;
    public static int position = 0;
    public static int labl_position[] = new int[100];

    //loader
    public static void main(String[] args) {
        //include contents in isa.txt
        try{
            BufferedReader br = new BufferedReader(new FileReader("isa.txt"));
            while(true){
                String line = br.readLine();
                if(line == null){
                    break;
                }
                ISA[cnt] = line;
                cnt++;
            }
        }catch(IOException ioe){
            System.out.println(ioe);
        }
        
        //set loader
        loader();
        
        //memory
        memory();
        
        //executing in cpu
        cpu();
    }//main
    
    
    //loader
    public static void loader(){
        //loader: 1)seperate top, middle, and bottom blocks
        String top[] = new String[100]; 
        int t1 = 0;
        String middle[][] = new String[100][4]; 
        int m1 = 0; int m2 = 0;
        String bottom[] = new String[100]; 
        int b1 = 0;
        int end1 = 0, end2 = 0;
        int tmp = 0;
        
        //find END
        for(int i = 0; i < cnt; i++){
            if(ISA[i].equals("END")){
                end1 = i;
                tmp= i+1;
                break;
            }
        }
        for(int i = tmp; i < cnt; i++){
            if(ISA[i].equals("END")){
                end2 = i;
            }
        }
        System.out.println("end1: "+end1+", end2: "+end2);
        
        
        //set up top[] for variables by StringTokenizer
        for(int i = 0; i < end1; i++){
            StringTokenizer st1 = new StringTokenizer(ISA[i]);
            while(st1.hasMoreTokens()){
                top[t1] = st1.nextToken();
                System.out.println("top["+t1+"]: "+top[t1]);
                t1++;
            }
            
        }
        //select variable name, assign to var_name[]
        for(int i = 0; i < t1; i++){
            if(i%4 ==1){
                var_name[vn1] = top[i];
                System.out.println("var_name["+vn1+"]: "+var_name[vn1]);
                vn1++;
            }
        }
        
        //set up middle block -> middle[][]
        for(int i = end1+1; i < end2; i++){
            StringTokenizer st2 = new StringTokenizer(ISA[i]);
            while(st2.hasMoreTokens()){
                middle[m1][m2] = st2.nextToken();
                m2++;
            }
            m1++;
            m2=0;
        }
        for(int i = 0; i < m1; i++){
            for(int j = 0; j < 4; j++){
                System.out.println("middle["+i+"]["+j+"]: "+middle[i][j]);
                if(middle[i][j] != null){
                    if(middle[i][j].equals("LABL")){
                        labl_position[i] = Integer.parseInt(middle[i][j+1]);
                        System.out.println("labl_possition["+i+"] is: "+Integer.parseInt(middle[i][j+1]));
                    }
                }
            }
        }
        //initialize memory[]
        for(int i = 0; i < memory.length; i++){
            memory[i] = "";
        }
        //construct 10 bit decimal on middle block with var_name[]
        for(int i = 0; i < m1; i++){
            for(int j = 0; j < 4; j++){
                if(middle[i][j] == null){
                    memory[m3] = memory[m3]+"000";
                }else{
                    memory[m3] = memory[m3] + encode(middle[i][j]);
                } 
            } 
            m3++;
        }
        
        
        //bottom block - bottom[]
        for(int i = end2+1; i < cnt; i++){
            bottom[b1]=ISA[i];
            System.out.println("bottom["+b1+"]: "+bottom[b1]);
            data[d1] = Float.parseFloat(bottom[b1]);
            System.out.println("data["+d1+"]: "+data[d1]);
            d1++;
            b1++;
        }
        
    }//loader
    
    public static String encode(String str){
        int i = 0; int j = 0; int k = 0;
        int tmp = 0;
        String result = "";
        
        //encode every operator -> 1 digit with sign & the others will be 3 digits
        if(str.equals("MOVE")){
            result = "+0";
        }else if (str.equals("ADD")){
            result = "+1";
        }else if (str.equals("SUB")){
            result = "-1";
        }else if (str.equals("MUL")){
            result = "+2";
        }else if (str.equals("DIV")){
            result = "-2";
        }else if (str.equals("READ")){
            result = "+3";
        }else if (str.equals("LABL")){
            result = "-3";
        }else if (str.equals("GE")){
            result = "+4";
        }else if (str.equals("LE")){
            result = "-4";
        }else if (str.equals("PUTA")){
            result = "+5";
        }else if (str.equals("GETA")){
            result = "-5";
        }else if (str.equals("LOOP")){
            result = "+6";
        }else if (str.equals("PRNT")){
            result = "+7";
        }else if (str.equals("STOP")){
            result = "-9";
        }else if (str.equals(var_name[0])){
            result = "001";
        }else if (str.equals(var_name[1])){
            result = "002";
        }else if (str.equals(var_name[2])){
            result = "003";
        }else if (str.equals(var_name[3])){
            result = "004";
        }else if (str.equals(var_name[4])){
            result = "005";
        }else if (str.equals(var_name[5])){
            result = "006";
        }else if (str.equals(var_name[6])){
            result = "007";
        }else{
            if(str.length() == 1)result = "00"+str;
            if(str.length() == 2)result = "0"+str;
            if(str.length() == 3)result = str;
        }
        return result;
    }//encode
    
    @Override
    public boolean equals(Object obj){
        return super.equals(obj);
    }
    //memory
    public static void memory(){
        //System.out.println("it's memory");
        for(int i = 0; i < m3; i++){
            System.out.println("memory["+i+"]: "+memory[i]);
        }
        
    }
    //cpu
    public static void cpu(){
        //System.out.println("it's cpu");
        String instruction = "";
        String opc = "";
        String op1 = "";
        String op2 = "";
        String op3 = "";
        int pc = 0;
        instruction = memory[pc];
        opc = instruction.substring(0,2);
        d1 = 0;
        m3 = 0;
        while(!"-9".equals(opc)){
        //Fetch instruction
        //System.out.println("pc: "+pc);
        instruction = memory[pc];
        opc = instruction.substring(0,2);
        op1 = instruction.substring(2,5);
        op2 = instruction.substring(5,8);
        if(instruction.length() > 10)op3 = instruction.substring(8,11);
        System.out.println("instruction: "+instruction);
        
        
        //Instruction decode
        System.out.println("opc: "+opc); 
        System.out.println("op1: "+op1);
        System.out.println("op2: "+op2); 
        System.out.println("op3: "+op3); 
        
        switch(opc){  //Fetch oprand, Execution, Store result     
            case "+3": //READ
              if(data[d1] != 0){
                var_val [stoi(op1)-1][0] = data[d1];
                d1++;
              }
              System.out.println(var_name [stoi(op1)-1]+": "+var_val [stoi(op1)-1][0]);
              //pc++;
              break;
            case "-3": //LABL
                System.out.println("position "+pc+" is a LABL: "+stoi(op1));
                labl_position[pc] = stoi(op1);
                break;
            case "-1": //SUB
                float sub_result = var_val[stoi(op1)-1][0] - var_val[stoi(op2)-1][0];
                var_val [stoi(op3)-1][0] = sub_result;
                int ind = stoi(op3)-1;
                System.out.println(var_name [ind]+": "+var_val [stoi(op3)-1][0]);
                break;
            case "+5": //PUTA, -PUTA P Q R: assign the contents in P into Rth element location in array Q
                int location = (int)var_val[(stoi(op3)-1)][0];
                var_val[stoi(op2)-1][location] = var_val[stoi(op1)-1][0];
                System.out.println(var_name [stoi(op2)-1]+location+": "+var_val [stoi(op2)-1][location]);
                break;
            case "+6": //LOOP, -LOOP P Q R: go to LABL R (Q-P) times
                float x = var_val[stoi(op2)-1][0] - var_val[(stoi(op1)-1)][0];                
                for(int i = 0; i < 50; i++){
                    if(labl_position[i] == stoi(op3)){
                        position = i;                          
                    }  
                }                
                loop_times = loop_times + 1;
                if(x > 1) {
                    pc = position;
                    var_val[1][0] = (float)(var_val[1][0]+1.0);
                }
                else{
                    loop_times = 0;
                    var_val[1][0] = 0;
                }
                break;
            case "+0": //MOVE, -MOVE P Q: assign the contents of P into Q
                var_val[stoi(op2)-1][0] = var_val[stoi(op1)-1][0];
                int ind4 = stoi(op2)-1;
                System.out.println(var_name [ind4]+": "+var_val [stoi(op2)-1][0]);
                break;
            case "+1": //ADD,  -ADD P Q R: add the contents of P and Q, then assign the result in R
                float add_result = var_val[stoi(op1)-1][0] + var_val[stoi(op2)-1][0];
                var_val [stoi(op3)-1][0] = add_result;
                int ind2 = stoi(op3)-1;
                System.out.println(var_name [ind2]+": "+var_val [stoi(op3)-1][0]);
                break;
            case "-2": //DIV, -DIV P Q R: divide the contents of P by that of Q, then assign the result n R
                float div_result = var_val[stoi(op1)-1][0] / var_val[stoi(op2)-1][0];
                var_val [stoi(op3)-1][0] = div_result; 
                int ind3 = stoi(op3)-1;
                System.out.println(var_name [ind3]+": "+var_val [stoi(op3)-1][0]);
                break;
            case "+4": //GE
                if(var_val[stoi(op1)-1][0]>= var_val[stoi(op2)-1][0]){
                    System.out.println(var_val[stoi(op1)-1][0]+">="+var_val[stoi(op2)-1][0]);
                    for(int i = 0; i < 50; i++){
                        if(labl_position[i] == stoi(op3)){
                            position = i; 
                        }  
                    }
                    pc = position;
                }else{
                    System.out.println(var_val[stoi(op1)-1][0]+"<"+var_val[stoi(op2)-1][0]);
                }
                break;
            case "-5": //GETA -GETA P Q R: assign the Qth element in array P into R
                float y = var_val[stoi(op2)-1][0];
                var_val[stoi(op3)-1][0] = var_val[stoi(op1)-1][(int)y];
                System.out.println(var_name[stoi(op3)-1]+": "+var_val[stoi(op3)-1][0]);
                break;
            case "+7": //PRINT -PRNT P: print the contents of P
                System.out.println(var_name[stoi(op1)-1]+": "+var_val[stoi(op1)-1][0]);
                break;               
            //next instruction
            }
        pc = pc+1;
        }
    } 
    
    public static int stoi(String str){
        int result = 0;
        result = Integer.parseInt(str);
        return result;
    }//stoi
}//Main
