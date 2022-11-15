import math
import numpy as np
import pandas as pd
import matplotlib.pyplot as plot
from matplotlib.colors import ListedColormap
import os
def main():
    #data = pd.read_csv(os.path.join(os.getcwd(),"P2","irisdata.cv"))
    data = pd.read_csv('P2\irisdata.csv')
    print("hi")
    # setosa = 1
    # versicolor = 2
    # virginica = 3
    data = data.replace(to_replace="setosa",value=1)
    data = data.replace(to_replace="versicolor",value=2)
    data = data.replace(to_replace="virginica",value=3)
    exerciseOne(data)
    

def exerciseOne(data):
    print("Exercise 1")

    #xData = data['petal_length']

    data = data[['petal_length','petal_width']].copy()

    kRange = 3

    # Chose random starting c_points
    np.random.seed(12345)

    c_points = data.sample(kRange)

    print(c_points)

    k = c_points.shape[0]
    n = data.shape[0]
    assign = []
    assign_errors = []

    for i in range(n):
        all_errors = np.array([])
        for c_point in range(k):
        
            #print("hi")

            error = math.dist(c_points.iloc[c_point,:],data.iloc[i,:])
            all_errors = np.append(all_errors,error)
            print('Error for centroid {0}: {1:.2f}'.format(i, error))

        nearest_c_point = np.where(all_errors==np.amin(all_errors))[0].tolist()[0]
        nearest_c_point_error = np.amin(all_errors)
        assign.append(nearest_c_point)
        assign_errors.append(nearest_c_point_error)
    #print(all_errors)
    #print(assign)

    data['c_point'] = assign
    data['error'] = assign_errors
    data.head()
    print(data)




   # print(list(data['species'].astype('category')))
    #print(data['species'])
    colnames = list(data.columns[1:-1])

    customcmap = ListedColormap(["crimson", "mediumblue", "darkmagenta"])
    
    fig, ax = plot.subplots(figsize=(8, 6))
    plot.scatter(x=data['petal_length'], y=data['petal_width'], s=150,
                c=data['c_point'].astype('category'), 
                cmap = customcmap)

    plot.scatter(c_points.iloc[:,0], c_points.iloc[:,1], marker ='s',s=300,
                c=[0,1,2], 
                cmap = customcmap, alpha =0.5)

    ax.set_xlabel(r'petal_length', fontsize=14)
    ax.set_ylabel(r'petal_width', fontsize=14)
    plot.xticks(fontsize=12)
    plot.yticks(fontsize=12)
    plot.show()

def exerciseTwo():
    print("Exercise 2")

def exerciseThree():
    print("Exercise 3")

def exerciseFour():
    print("Exercise 4")

def extraCredit():
    print("Extra Credit")

if __name__ == "__main__":
    main()