import java.util.Scanner;

public class Fraction {
    private int numerator;
    private int denominator;

    public Fraction(String strFract) {
        this.numerator = Integer.parseInt(strFract.substring(0, strFract.indexOf("/")));
        this.denominator = Integer.parseInt(strFract.substring(strFract.indexOf("/")+1));
    }

    private Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public String printFractionion() { return this.numerator + "/" + this.denominator;}

    public int getNumerator(){ return this.numerator; }

    public int getDenominator(){ return this.denominator;}

    public static Fraction normalization(Fraction fract){
        int den = fract.denominator;
        int num = fract.numerator;
        int minor = Math.min(Math.abs(fract.denominator), Math.abs(fract.numerator));
        for (int i = minor; i > 0; i--) {
            if ((den % i == 0) && (num % i == 0)){
                den = den / i;
                num = num / i;
            }
        }
        if (den < 0 && num < 0){
            den = Math.abs(den);
            num = Math.abs(num);
        }
        return new Fraction(num, den);
    }

    public static String summation(Fraction first, Fraction second){
        int f_num = first.getNumerator(); int f_den = first.getDenominator();
        int s_num = second.getNumerator(); int s_den = second.getDenominator();
        Fraction result;

        if( f_den  == s_den) {
            return  normalization(new Fraction((f_num + s_num), f_den)).printFractionion();
        } else{
            return normalization(new Fraction((f_num * s_den + s_num * f_den), f_den * s_den)).printFractionion();
        }
    }

    public static String subtraction(Fraction first, Fraction second){
        int f_num = first.getNumerator(); int f_den = first.getDenominator();
        int s_num = second.getNumerator(); int s_den = second.getDenominator();

        if( f_den  == s_den) {
            return normalization(new Fraction((f_num - s_num), f_den)).printFractionion();
        } else{
            return normalization(new Fraction((f_num * s_den - s_num * f_den), f_den * s_den)).printFractionion();
        }
    }

    public static String multiplication(Fraction first, Fraction second){
        return  normalization(new Fraction((first.getNumerator() * second.getNumerator()), (first.getDenominator() * second.getDenominator()))).printFractionion();
    }

    public static String division(Fraction first, Fraction second){
        return normalization(new Fraction((first.getNumerator() * second.getDenominator()), (second.getNumerator() * first.getDenominator()))).printFractionion();
    }
}
