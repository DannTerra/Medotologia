/*
 * Exercício: "Intervalos de Confiança" - questão 3
 * Disciplina: Metodologia Científia (2021/1)
 * Autora: Daniela Costa Terra
*/
//package exec3IntervalosConfianca;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import org.apache.commons.math3.distribution.TDistribution;

public class Exec3IntervalosDeConfianca {
   
   //Busca o valor aproximado do conjunto de dados  x
   //para um CI baseado em uma t_distribuition_cdf()
   //Retorno - IC:[A, B], onde:
   // A = calc_CI_T_Distribuition()[0] e B = calc_CI_T_Distribuition()[1] 
   public static double[] calc_CI_T_Distribuition(double[] data, double alpha) throws Exception{
       //ordena vetor data (usando função sort())
       Arrays.sort(data);
       int v = data.length-1;
       int index_AlphaDiv2 = searchX_TDistribuition_cdf(data, (alpha/2), v, 0, data.length-1);
       double pMean = 0.5;  //t_cdf(media esperada)
       int mu = searchX_TDistribuition_cdf(data, pMean, v, index_AlphaDiv2, data.length-1);
       double interval = data[mu] - data[index_AlphaDiv2];
       return new double[] {(data[mu] - interval), (data[mu] + interval)};
   }
   
   //Busca para aproximar  t_distribuition_cdf(x) > alpha 
   //em data previamente ordenado
   public static int searchX_TDistribuition_cdf(double[] data, double alpha, int v,
                                            int iMin, int iMax) throws Exception{
       for(int i=iMin; i <= iMax; i++){
           double pX = new TDistribution(v).cumulativeProbability(data[i]);
           if (pX >= alpha)
               return i;
       } 
      throw new Exception ("Erro: searchXAlpha não encontrou alpha: "+(alpha)); 
   }
   
    
  /*Método principal:
    Obtem valores a partir de arquivo (linha de comando), se informado 
  */  
  public static void main(String[] args) {
       double v[];
       if (args.length != 0 && args[0].length() != 0){
           v = leArquivo(args[0]);
       } else{
           double x[] = {-1,-1,-1,-3,-3,-3,-2,-2,-2, -7,-5, -5.7, 0, 0.4, 0.5, 0.7, 1, 1, 1, 1,1.5, 2,2.2,2.7, 3, 3.1, 3.2, 3.5, 3.7, 4, 5, 6, 7, 8};
            v = x; 
       }      
       Arrays.sort(v); // ordena o vetor
       System.out.print("Dados da amostra: ");imprime(v); System.out.println("");
       double mu = mean(v);
       double std_dev = std_dev(v);
       System.out.printf("\nTamanho da amostra: %d",v.length);
       System.out.printf("\nMédia: %.20f",mu);
       System.out.printf("\nDesvio padrão: %.20f",std_dev);
       //Exibe distribuição CDF dos dados de v:
       for(int i=0; i < v.length; i++){
           System.out.printf("TDistribution-cdf[%.3f]: %.20f \n", v[i],
                new TDistribution(v.length - 1).cumulativeProbability(v[i]));
       }
       
       //Calcula CI com alpha dado
       double alpha = 0.05;
       try{
           double CI[] = calc_CI_T_Distribuition(v, alpha);
           System.out.printf("\n\n Para alpha=%.2f o IC aproximado para"
                   + " a média é [%.4f, %.4f]\n", alpha, CI[0], CI[1]);
       }catch (Exception ex){
           System.out.println("Erro: "+ex.getMessage());
       }
  }

    
   /* Retorna a probabilidade de um ponto ocorrer em uma distribuição normal
    * para uma média (mu) e um desvio padrão (std_dev) dados */    
   public static double normal_distribution_pdf(double x, double mu, double std_dev){
       double expoent = -1*Math.pow((x - mu),2)/(2*Math.pow(std_dev,2));
       double p = (1/(Math.sqrt(2*Math.PI*std_dev*std_dev)))*Math.exp(expoent);       
       return p;
   }
   
   /* Retorna a probabilidade de um ponto ocorrer em uma distribuição normal
    * de média 0 e desvio padrão (std_dev = 1)                              */    
   public static double normal_distribution_pdf(double x){
       double expoent = -1*Math.pow(x,2)/2;
       //double p = (1/(Math.sqrt(2*Math.PI)))*Math.pow(Math.E,expoent);       
       double p = (1/(Math.sqrt(2*Math.PI)))*Math.exp(expoent);       
       return p;
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