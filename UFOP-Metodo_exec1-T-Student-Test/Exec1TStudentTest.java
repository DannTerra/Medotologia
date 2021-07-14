//package exec1TStudentTest;

/*
 * Exercício: "Intervalos de Confiança" - questão 3
 * Disciplina: Metodologia Científia (2021/1)
 * Autora: Daniela Costa Terra
*/
 

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import org.apache.commons.math3.distribution.TDistribution;

public class Exec1TStudentTest {
    
  /*
  *  Calcula o teste T-Student assumindo que os verdadeiros
  *  desvios padrão das duas amostras são iguais
  */   
  public static void two_samples_t_test_equal_sd(
        double Sm1,       // Sm1 = Sample 1 Mean.
        double Sd1,       // Sd1 = Sample 1 Standard Deviation.
        int Sn1,     // Sn1 = Sample 1 Size.
        double Sm2,       // Sm2 = Sample 2 Mean.
        double Sd2,       // Sd2 = Sample 2 Standard Deviation.
        int Sn2,       // Sn2 = Sample 2 Size.
        double alpha)     // alpha = Significance Level.
  {
    System.out.printf("Analise bilateral.\nAlpha= %.3f\n", alpha);
    // Degrees of freedom:
    double v = Sn1 + Sn2 - 2;
    System.out.printf("Degrees of Freedom = %f\n", v);
    // Pooled variance:
    double sp = Math.sqrt(((Sn1-1) * Sd1 * Sd1 + (Sn2-1) * Sd2 * Sd2) / v);
    System.out.printf("Pooled Standard Deviation =  %.20f\n", sp);
    // t-statistic:
    double t_stat = (Sm1 - Sm2) / (sp * Math.sqrt(1.0 / Sn1 + 1.0 / Sn2));
    System.out.printf("T Statistic =  %.20f\n", t_stat);  

    TDistribution dist = new TDistribution(v);
    double q = 0.5 - dist.cumulativeProbability(Math.abs(t_stat));
    System.out.printf("Probabilidade da diferença das médias ser devido a chance= %.2f", (2*q));
    
    //Testa hipótese Nula (Sm1 == Sm2) 
    if (q  < (alpha/2))
          System.out.println("\nHipótese Nula:  REJECTED");
    //Testa hipótese Alternativa (Sm1 != Sm2) 
    if (q > (alpha / 2)){
          System.out.println("Hipótese Alternativa (Sm1 != Sm2):  REJECTED");
    }else{
          System.out.println("Hipótese Alternativa (Sm1 != Sm2):  NOT REJECTED");
    }    
    
    //Testa hipótese Alternativa (Sm1 < Sm2) 
    double r = dist.cumulativeProbability(Math.abs(t_stat)); 
    if (r > alpha){
          System.out.println("Hipótese Alternativa (Sm1 < Sm2):  REJECTED");
    }else{
          System.out.println("Hipótese Alternativa (Sm1 < Sm2):  NOT REJECTED");
    }
    
    //Testa hipótese Alternativa (Sm1 > Sm2) 
    double s = 1- dist.cumulativeProbability(Math.abs(t_stat)); 
    if (s > alpha){
          System.out.println("Hipótese Alternativa (Sm1 > Sm2):  REJECTED");
    }else{
          System.out.println("Hipótese Alternativa (Sm1 > Sm2):  NOT REJECTED");
    }
  }
    
  /*Método principal:
    Obtem valores a partir de arquivo (linha de comando), se informado 
  */  
  public static void main(String[] args) {
       double v1[], v2[];
       if (args.length!=0 && args[0].length()!=0 && args[1].length()!=0){
            v1 = leArquivo(args[0]);
            v2 = leArquivo(args[1]);
       } else{
            double x1[] = { 3, 3.1, 3.2, 3.5, 3.7, 4, 5, 6, 7, 8};
            v1 = x1; 
            double x2[] = { 3, 3.1, 3.2, 3.5, 3.7, 4, 5, 6, 7, 8};
            v2 = x2; 
       }      
       Arrays.sort(v1); // ordena o vetor
       System.out.print("Dados da amostra 1: \n");imprime(v1); 
       double mu1 = mean(v1);
       double std_dev1 = std_dev(v1);
       System.out.printf("\nTamanho da amostra: %d",v1.length);
       System.out.printf("\nMédia: %.20f",mu1);
       System.out.printf("\nDesvio padrão: %.20f",std_dev1);
       
       Arrays.sort(v2); // ordena o vetor
       System.out.print("\n\nDados da amostra 2: \n");imprime(v2);
       double mu2 = mean(v2);
       double std_dev2 = std_dev(v2);
       System.out.printf("\nTamanho da amostra: %d",v2.length);
       System.out.printf("\nMédia: %.20f",mu2);
       System.out.printf("\nDesvio padrão: %.20f\n",std_dev2);
       
       System.out.println("\n------------------------------------------------"); 
       System.out.println("Student t test for two samples (equal variances):"); 
       System.out.println("-------------------------------------------------");               
       //Confiabilidade de 95% para CI em T-Student Test
       double alpha = 0.05;
       //Calcula estatística do teste T-Student assuindo amostras com igual
       //variância
       two_samples_t_test_equal_sd(mu1, std_dev1, v1.length, 
               mu2, std_dev1, v2.length, alpha);
         
  }

  
    
    
   /* Retorna o intervalo de valores [mínimo, máximo] do vetor */ 
   public static double[] interval(double[] data){
       //quicksort(data);
       double mim = data[0], max= data[0];
       for(int i= 1; i < data.length; i++){
           if(data[i] < mim){
               mim = data[i];
           }else if (data[i] > max)
               max = data[i];
           
       }
       return new double[]{mim, max};
   }
   
   /* Retorna o variância dos dados população*/ 
   public static double variance(double[] data){ 
       double mean = mean(data);
       double sigma=0;
       for(double x: data){
           sigma+= Math.pow((x - mean), 2);
       }
       return sigma/(data.length-1);
   }
   
  /* Retorna o desvio padrão  população*/
  public static double std_dev(double[] data){
      return (Math.sqrt(variance(data)));
  }
    
 /* Retorna a média aritmética */
  public static double mean(double v[]){
      double s=0;
      for(double x: v) s+=x;
      return (s/v.length);
  }
  
  /* Retorna a mediana, ou seja, o 50º percentil */
  public static double median(double v[]){
       return percentile(v, 50);
  }
  
  /* Retorna o p-ésimo percentil após ordenar os dados  */
  public static double percentile(double v[], int p){
      int i = (int) Math.floor(v.length* p/100.); //100.0 para evitar div inteira
      int index = (i > 0? (i-1): 0);
      double value = nth_element(v, index);
      return (value);
  }
  
  /* Retorna a primeira moda (moda única) ou NaN se não   
    houver elementos com frequencia maior que 1. */
  public static double mode(double v[]){
     //quicksort(v);
     double n=v[0], nAux=v[0];
     int f=1, fAux =-1;
     for(int i= 1; i < v.length; i++){
          if (v[i] == n){
              f++;
          }else{ 
            if (fAux >= f){
                f=1; n = v[i];
            }else if (fAux < f){
               fAux = f; nAux = n;
               f=1; n = v[i];
            } 
          }  
     }
     int freq = (fAux !=-1 && fAux >= f)? fAux: f;
     if (freq <= 1)
         return Double.NaN;
     double mode = (fAux !=-1 && fAux >= f)? nAux: n;
     return mode;
  }
    
  /*  
   * Classe interna auxiliar usada no método particao()
   * para retornar limites inferior e superior  
  */
  private static class LimiteParticoes { 
      int i,j, pivo; 
  }
   
  private static LimiteParticoes partition (double v[], int esq, int dir) {
    LimiteParticoes p = new LimiteParticoes ();
    p.i = esq;  p.j = dir;
    double x = v[(p.i + p.j) / 2]; // @{\it obtêm o pivo x}@
    do {
      while (x > (v[p.i])) p.i++;
      while (x < (v[p.j])) p.j--;
      //realiza troca de partições:
      if (p.i <= p.j) {
        double w = v[p.i]; v[p.i] = v[p.j]; v[p.j] = w;
        p.i++; p.j--;
      }
    } while (p.i <= p.j);
    if(x == v[p.i])  p.pivo = p.i;
    else if (x == v[p.j]) p.pivo = p.j;
    else p.pivo = p.j+1;
    return p;
  }

  public static double nth_element (double v[], int nth) {
     LimiteParticoes p=null;
     int first = 0, last = v.length-1;
     while((last - first) > 3){
          p = partition(v, first, last); 
          if (p.pivo == nth)
              return v[p.pivo];
          if (p.pivo < nth)
              first = p.pivo;
          else
              last = p.pivo;
     }
     insertionSort(v, first, last);   
     return (v[nth]);
  }
  
  private static void insertionSort(double v[], int first, int last){
        double x, temp;
        for(int i= first+1; i<= last; i++){
            x = v[i];
            int j = i-1;
            for(;((j >= first) && (x < v[j])); j--){
                v[j+1] = v[j];
            }
            v[j+1] = x;            
        }
  }
  
  public static void imprime(double v[]){
      for(double x: v) System.out.print(x+" ");
  }
  
  public static double[] leArquivo(String nome){
      double v[] = null;
       try{
           System.out.println("file:" + nome);
           Vector<Double> dados = new Vector<Double>();
           Scanner file = new Scanner(new File(nome));
           while(file.hasNext()){
               dados.add(file.nextDouble());
           }
           if (dados.size() == 0){
               throw new Exception("Arquivo vazio!");
           }
           v = new double[dados.size()];
           for(int i=0; i < v.length ; i++){ v[i] = dados.get(i); }
           
       }catch(Exception ex){
           System.out.println("Erro na abertura do arquivo: "+nome+
             "Insira apenas o nome e extensão, se arquivo no diretorio local");
             System.exit(0);
       }
       return v;
  }
}