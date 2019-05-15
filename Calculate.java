public class Calculate {
    public static float[] vecmatmult(float[] v, float[][] m) {
        float[] r = new float[]{(m[0][0]*v[0])+(m[0][1]*v[1])+(m[0][2]*v[2])+(m[0][3]*v[3]),(m[1][0]*v[0])+(m[1][1]*v[1])+(m[1][2]*v[2])+(m[1][3]*v[3]),(m[2][0]*v[0])+(m[2][1]*v[1])+(m[2][2]*v[2])+(m[2][3]*v[3]),(m[3][0]*v[0])+(m[3][1]*v[1])+(m[3][2]*v[2])+(m[3][3]*v[3])};
        return r;
    }
    public static double[] vecmatmultDouble(double[] v, double[][] m) {
        double[] r = new double[]{(m[0][0]*v[0])+(m[0][1]*v[1])+(m[0][2]*v[2])+(m[0][3]*v[3]),(m[1][0]*v[0])+(m[1][1]*v[1])+(m[1][2]*v[2])+(m[1][3]*v[3]),(m[2][0]*v[0])+(m[2][1]*v[1])+(m[2][2]*v[2])+(m[2][3]*v[3]),(m[3][0]*v[0])+(m[3][1]*v[1])+(m[3][2]*v[2])+(m[3][3]*v[3])};
        return r;
    }
    public static double[] slowvecmath(float[] v, float[][] m) {
        double tempadd = 0;
        double[] result = new double[4];
        for (int r1=0; r1<v.length; r1++) {
            for (int c1=0; c1<v.length; c1++) {
                tempadd += m[r1][c1]*v[c1];
            }
            result[r1]=tempadd;
            tempadd = 0;
        }
        return result;
    }
    public static float[] translate(float[] vector, double x, double y, double z) {
        float[][] matrix = new float[][]{{1,0,0,(float)x},
                                         {0,1,0,(float)y},
                                         {0,0,1,(float)z},
                                         {0,0,0,1}};
        return vecmatmult(vector,matrix);   
    }
    public static double[] translateDouble(double[] vector, double x, double y, double z) {
        double[][] matrix = new double[][]{{1,0,0,x},
                                         {0,1,0,y},
                                         {0,0,1,z},
                                         {0,0,0,1}};
        return vecmatmultDouble(vector,matrix);   
    }
    public static float[] scale(float[] vector, double x, double y, double z) {
        float[][] matrix = new float[][]{{(float)x,0,0,0},
                                         {0,(float)y,0,0},
                                         {0,0,(float)z,0},
                                         {0,0,0,1}};
        return vecmatmult(vector,matrix);  
    }
    public static float[] rotate(float[] vector, char c, double angle) {
        float[][] matrix = {};
        float asin=(float)(Math.sin(angle));
        float acos=(float)(Math.cos(angle));
        if (c=='x') {
            matrix = new float[][]{{1,0,0,0},
                                   {0,acos,-1*asin,0},
                                   {0,asin,acos,0},
                                   {0,0,0,0}};
        }
        if (c=='y') {
            matrix = new float[][]{{acos,0,asin,0},
                                   {0,1,0,0},
                                   {-1*asin,0,acos,0},
                                   {0,0,0,0}};
            
        }
        if (c=='z') {
            matrix = new float[][]{{acos,-1*asin,0,0},
                                   {asin,acos,0,0},
                                   {0,0,1,0},
                                   {0,0,0,0}};
            
        }
        return vecmatmult(vector,matrix);  
    }
    public static double[] rotateDouble(double[] vector, char c, double angle) {
        double[][] matrix = {};
        double asin=(Math.sin(angle));
        double acos=(Math.cos(angle));
        if (c=='x') {
            matrix = new double[][]{{1,0,0,0},
                                   {0,acos,-1*asin,0},
                                   {0,asin,acos,0},
                                   {0,0,0,0}};
        }
        if (c=='y') {
            matrix = new double[][]{{acos,0,asin,0},
                                   {0,1,0,0},
                                   {-1*asin,0,acos,0},
                                   {0,0,0,0}};
            
        }
        if (c=='z') {
            matrix = new double[][]{{acos,-1*asin,0,0},
                                   {asin,acos,0,0},
                                   {0,0,1,0},
                                   {0,0,0,0}};
            
        }
        return vecmatmultDouble(vector,matrix);  
    }
    public static double[] project2Ddouble(double[] point, double fov, double aspect, double near, double far) {
        double fn=(far-near);
        double mathtf=(Math.atan(fov/2));
        double[] result = new double[]{(mathtf*point[0]),(mathtf*point[1]),((-1*((far+near)/fn))*point[2])+((-1*((2*(far*near)))*point[3]))};
        double x = -1*(result[0]/(-1*point[2]));
        double y = -1*(result[1]/(-1*point[2]));
        x+=1;
        x/=2;
        y+=1;
        y/=2;
        return(new double[]{x,y});
    }
    public static double[] project2D(float[] vector, double fov, double aspect, double near, double far) {
        float fn=(float)(far-near);
        float mathtf=(float)(Math.atan(fov/2));
        double[] result = new double[]{(mathtf*vector[0]),(mathtf*vector[1]),((-1*(float)((far+near)/fn))*vector[2])+((-1*(float)((2*(far*near)))*vector[3]))};
        double x = -1*(result[0]/(-1*vector[2]));
        double y = -1*(result[1]/(-1*vector[2]));
        x+=1;
        x/=2;
        y+=1;
        y/=2;
        return(new double[]{x,y});
    }
    public static void vecmatmulttest(int runs) {
        float[][] matrix = new float[][]{{(float)Math.random(),(float)Math.random(),(float)Math.random(),(float)Math.random()},
                                               {(float)Math.random(),(float)Math.random(),(float)Math.random(),(float)Math.random()},
                                               {(float)Math.random(),(float)Math.random(),(float)Math.random(),(float)Math.random()},
                                               {(float)Math.random(),(float)Math.random(),(float)Math.random(),(float)Math.random()}};
        float[] vector = new float[]{(float)Math.random(), (float)Math.random(), (float)Math.random(), (float)Math.random()};
        long fastsum=0;
        long slowsum=0;
        long projsum=0;
        for(int i=0;i<runs;i++) {
            long startf = System.currentTimeMillis();
            int calcf=0;
            while(System.currentTimeMillis()-1000<startf) {
                vecmatmult(vector,matrix);
                calcf++;
            }
            long starts = System.currentTimeMillis();
            int calcs=0;
            while(System.currentTimeMillis()-1000<starts) {
                slowvecmath(vector,matrix);
                calcs++;
            }
            long startp = System.currentTimeMillis();
            int calcp=0;
            while(System.currentTimeMillis()-1000<startp) {
                project2D(vector,90,1,0,100);
                calcp++;
            }
            fastsum+=calcf;
            slowsum+=calcs;
            projsum+=calcp;
            System.out.println("fastmath calculations per second: " + calcf);
            System.out.println("slowmath calculations per second: " + calcs);
            System.out.println("projections per second: " + calcp);
        }
        System.out.println("\n\nAverage fastmath cps: " + fastsum/runs + "\nAverage slowmath cps: " + slowsum/runs + "\nAverage projection cps: " + projsum/runs);
    }
    public static void main(String[] args) {
        vecmatmulttest(Integer.parseInt(args[0]));
    }
}