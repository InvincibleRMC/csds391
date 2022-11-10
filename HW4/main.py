import numpy as np
import random
import math
def main():
    #Cherries = 0
    #Lime = 1
    print("hi")
    d1 = [0]*100
    d2 = [0]*75+[1]*25
    d3 = [0]*50+[1]*50
    d4 = [0]*25+[1]*75
    d5 = [1]*100
    """print(d1)
    print(d2)
    print(d3)
    print(d4)
    print(d5)
    """    
    random.shuffle(d1)
    random.shuffle(d2)
    random.shuffle(d3)
    random.shuffle(d4)
    random.shuffle(d5)

    unique, counts = np.unique(d3,return_counts=True)
    print(dict(zip(unique, counts))[1])
    h1=0.1
    h2=0.2
    h3=0.4
    h4=0.2
    h5=0.1
    heuristics = [h1,h2,h3,h4,h5]

    plot(d1,heuristics)
    plot(d2,heuristics)
    plot(d3,heuristics)
    plot(d4,heuristics)
    plot(d5,heuristics)

4+3+2+1


def plot(arr,heuristics):
    for i in range(len(arr)+1):
        newArr = arr[0:i]
        #unique, counts = np.unique(newArr,return_counts=True)
        limeCount = np.count_nonzero(newArr)
        for h in heuristics:
            print(limeCount)
            h= h*math.comb()




if __name__ == "__main__":
    main()