# Disciplina:  Metodologia de Pesquisa
# EXERCÍCIO2:  "Intervalos de Confiança"
# Autora: Daniela Costa Terra

# Imports
import matplotlib.pyplot as plt
import numpy as np
import random
import math
import scipy.special as hyp2f1
from scipy.stats import t

# Testes executados:
def main():
    x1 = 1
    x2 = 2
    v=2
    #Testa função definida: t_distribution_pdf(x1=1, 2)
    print('Se x={0} e v={1} t_distribution_pdf({0}, {1}) = {2}'.format(x1, v, t_distribution_pdf(x1, v)))
     
    #Testa função definida: t_distribution_cdf(x1=1, 2)
    print('Se x={0} e v={1} t_distribution_cdf({0}, {1}) = {2}'.format(x1, v, t_distribution_cdf(x1, v)))
    # Testado contra função da biblioteca: print (t.cdf(x1, v))

    print('Se x1={0}, x2={1} e v={2} t_distribution_cdf({0}, {1}, {2}) = {3}'.format(x1, x2, v, t_distribution_cdf_intervalo(x1, x2, v)))
    # Testado contra função da biblioteca: print (t.cdf(x2, v) - t.cdf(x1, v))
     
    

# Função de probabilidade de um valor estar entre -infinito e um certo ponto 'p'
# em uma distribuição-t com 'v' graus de liberdade (PDF)
def t_distribution_cdf_intervalo(x1, x2, v):
    distT_cdf1 = t_distribution_cdf(x1, v);
    distT_cdf2 = t_distribution_cdf(x2, v);
    if distT_cdf1 < distT_cdf2:
        return  (distT_cdf2 - distT_cdf1)
    else:
        return  (distT_cdf1 - distT_cdf2)

# Função de probabilidade de um valor estar entre -infinito e um certo ponto 'p'
# em uma distribuição-t com 'v' graus de liberdade (PDF)
def t_distribution_cdf(x, v):
    aux = x*math.gamma((v+1)/2.0)
    f2f1 = hyp2f1.hyp2f1((1/2.0), ((v+1)/2), (3/2.0), (-1*(x*x/v)))
    den = math.sqrt(math.pi*v)*math.gamma(v/2)
    return 1/2+ aux*(f2f1/den)

# Função de probabilidade de um ponto ser 'p' em uma distribuição-t
# com 'v' graus de liberdade (PDF)
def t_distribution_pdf(x, v):
    pot= math.pow((1 + x*x/v),-1*((v+1)/2)) 
    num= math.gamma((v+1)/2)
    den= math.sqrt(v*math.pi)*math.gamma(v/2)
    return (num/den*pot)
    
#Calcula a média de uma lista de double
def mean(data):
    soma = 0
    for x in data:
        soma = soma + x
    mean = soma / len(data)   
    return mean

#Calcula desvio padrão da amostra para uma lista de double
def std_dev(data, mean):
    soma = 0
    for x in data:
        soma = soma + math.pow(x - mean,2)
    soma = soma/(len(data)-1)
    std_dev = math.sqrt(soma)
    return std_dev



if __name__ == '__main__':  
    main()                   

