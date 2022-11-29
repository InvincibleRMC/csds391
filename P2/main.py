import math
import random
import numpy as np
import pandas as pd
from pandas import DataFrame
import matplotlib.pyplot as plot
from matplotlib.colors import ListedColormap

# Extra Credit imports
import tensorflow as tf
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split



def main():
    
    data: DataFrame = pd.read_csv('P2\irisdata.csv')
    # setosa = -1
    # versicolor = 0
    # virginica = 1
    data: DataFrame = data.replace(to_replace="setosa",value=-1)
    data: DataFrame = data.replace(to_replace="versicolor",value=0)
    data: DataFrame = data.replace(to_replace="virginica",value=1)

    #exerciseOne(data)
    #exerciseTwo(data)
    #exerciseThree(data)
    #exerciseFour(data)
    extraCredit(data)
    
    

# Plot Data and Center Points
def customPlot(data: DataFrame,center_points: DataFrame):

    customcmap = ListedColormap(["red", "blue", "darkmagenta"])
    
    fig, axis = plot.subplots(figsize=(8, 6))
    # After Startup Color Data
    if('center_point' in data.columns):
        plot.scatter(data.iloc[:,0], data.iloc[:,1],  marker = 'o', 
                    c=data['center_point'].astype('category'), 
                    cmap = customcmap, s=80, alpha=0.5)
    else:
        plot.scatter(data.iloc[:,0], data.iloc[:,1],  marker = 'o')

    plot.scatter(center_points.iloc[:,0], center_points.iloc[:,1],  
                marker = 's', s=200, c=range(len(center_points.iloc[:,0])), 
                cmap = customcmap)
    axis.set_xlabel(r'petal_length', fontsize=20)
    axis.set_ylabel(r'petal_width', fontsize=20)
    plot.show(block=False)
    plot.pause(1)
    plot.close()

# Get k Random Starting Points
def initiate_center_points(k: int, data: DataFrame):
    center_points = data.sample(k)
    return center_points

# Find the Distances between Points
# I tried math.dist with no success
def rsserr(x,y):
    return np.square(np.sum((x-y)**2)) 
    
# Assign Center Points
def center_point_assignation(data: DataFrame, center_points: DataFrame):
    k: int = center_points.shape[0]
    n: int = data.shape[0]
    assignation = []
    assign_errors = []

    # caculate error for Points
    for obs in range(n):
        
        all_errors = np.array([])
        # caculate error for all Center Points
        for center_point in range(k):
            # caculate error for one Center Point
            err: float = rsserr(center_points.iloc[center_point, :], data.iloc[obs,:])
            all_errors = np.append(all_errors, err)

       
        nearest_center_point =  np.where(all_errors==np.amin(all_errors))[0].tolist()[0]
        nearest_center_point_error = np.amin(all_errors)
        
        assignation.append(nearest_center_point)
        assign_errors.append(nearest_center_point_error)

    return assignation, assign_errors

def kmeans(data: DataFrame, k: int = 2, tol: float = 1e-5):
    
    # Copys Data Again
    working_data: DataFrame = data.copy()
    err: list = []
    j: int = 0
    
    # Randomly assign starting points
    center_points: DataFrame = initiate_center_points(k, data)
    # Initial Graph
    customPlot(working_data,center_points)

    while(True):
        
        # Assign Center Points
        working_data['center_point'], j_err = center_point_assignation(working_data, center_points)
        err.append(sum(j_err))
        
        # Updating Centroid Position
        center_points = working_data.groupby('center_point').agg('mean').reset_index(drop = True)

        # Intermediate Graphs
        customPlot(working_data,center_points)

        if err[j-1]-err[j]<=tol and j>0:
            break
        j+=1

    # Final Data
    working_data['center_point'], j_err = center_point_assignation(working_data, center_points)
    center_points = working_data.groupby('center_point').agg('mean').reset_index(drop = True)
    
    return working_data['center_point'], j_err, center_points, err

# Plot The Error Over Time Graph
def errorPlot(err):
    fig, axis = plot.subplots(figsize=(8, 6))
    plot.plot(range(len(err)), err,  marker = 'o') 
    
    axis.set_xlabel(r'Step Count', fontsize=20)
    axis.set_ylabel(r'Error', fontsize=20)
    plot.show(block=False)
    plot.pause(5)
    plot.close()

# Code For Exercise One
def exerciseOne(startingData: DataFrame):
    print("Exercise 1")
    # Selects petal_length and petal_width columns
    data: DataFrame = startingData[['petal_length','petal_width']].copy()
    np.random.seed(34352235)
    data['center_point'], data['error'], center_points, error =  kmeans(data, k=2,tol=1e-4)
    data.head()
    # Plots final clustering and error plot
    customPlot(data,center_points)
    errorPlot(error)


def singleLayer(Xs: np.ndarray, weight: np.ndarray):

    y = np.zeros(len(Xs))
    # Sum across weights for all x values
    for j in range(len(Xs)):
        y[j] = weight*Xs[j]
    
    return y

# Plot line and Data
def plotXVsY(data: DataFrame, Multithing, weight2):

    customcmap = ListedColormap(["green","blue"])
    fig, axis = plot.subplots(figsize=(8, 6))
    
    line = Multithing*-1/weight2


    plot.plot(data['petal_length'],line)
    plot.scatter(data['petal_length'], data['petal_width'],  marker = 'o', 
                    c=data['species'].astype('category'), 
                    cmap = customcmap, s=80)

    axis.set_xlabel(r'petal_length', fontsize=20)
    axis.set_ylabel(r'petal_width', fontsize=20)
    plot.xlim((2.75,7.25))
    plot.ylim((0.75,2.75))
    plot.show(block=False)
    plot.pause(5)
    plot.close()

# Calulates sigmoid
def sigmoid(data,weights):
   
    k = multiLine(data,weights)
    t = np.zeros(len(k))
    for i in range(len(k)):
        t[i] = 1/(1+math.exp(-k[i]))
    return t

# Code For Exercise Two
def exerciseTwo(startingData: DataFrame):
    print("Exercise 2")
    
    data: DataFrame = startingData[['petal_length','petal_width','species']].copy()
    
    # Remove Setosa
    removedAMount: int = len(data[(data['species'] == -1)])
    data.drop(data[(data['species'] == -1)].index, inplace=True)
    data.index = data.index - removedAMount


    customcmap = ListedColormap(["green","blue"])
    fig, axis = plot.subplots(figsize=(8, 6))
    plot.scatter(data['petal_length'], data['petal_width'],  marker = 'o', 
                    c=data['species'].astype('category'), 
                    cmap = customcmap, s=80)

    axis.set_xlabel(r'petal_length', fontsize=20)
    axis.set_ylabel(r'petal_width', fontsize=20)
    plot.show(block=False)
    plot.pause(1)
    plot.close()

    # Weights
    bias = -8.4
    weight = [-8.4,1.35,1]
    
    xOne = data['petal_length'].to_numpy()
    xZero = np.ones(len(xOne))
    xTwo = data['petal_width'].to_numpy()
    line = singleLayer(xZero,bias)
    line = line +singleLayer(xOne,weight[1])
    
    plotXVsY(data,line,weight[2])
    
    
    Xs = [xZero,xOne,xTwo]
    data['classification'] = sigmoid(Xs,weight)
    
    fig = plot.figure(figsize = (8,6))
    ax = plot.axes(projection='3d')
    ax.grid()

    
    ax.plot_trisurf(data['petal_length'],data['petal_width'], data['classification'])
    ax.set_title('3D Iris Data Plot')

    # Set axes label
    ax.set_xlabel('petal_length', labelpad=20)
    ax.set_ylabel('petal_width', labelpad=20)
    ax.set_zlabel('Class', labelpad=20)
    plot.show(block=False)
    plot.pause(5)
    plot.close()


   
    # Uncomment to see all classifications
    # sorteddata =data.sort_values(by=['classification'], ascending=True)
    # print(sorteddata.to_string())

    # Not Close
    print("Not Close")
    print(data.loc[48],"\n")
    print(data.loc[85],"\n")

    # Correct Close
    print("Correct Close")
    print(data.loc[2],"\n")
    print(data.loc[92],"\n")

    # Incorrect Close
    print("Incorrect Close")
    print(data.loc[33],"\n")
    print(data.loc[69],"\n")

def multiLine(data: np.ndarray,weights: np.ndarray):
    
    data = np.array(data)
    if(data.ndim == 1):
        y = np.zeros(len(data))
        y = singleLayer(data,weights)
    else:
        y = np.zeros(len(data[0]))
        for i in range (len(weights)):
            y = y + singleLayer(data[i],weights[i])
    
    
    return y
    
# Finds meanSquare
def meanSquare(data: np.ndarray,weights: np.ndarray, patternClass: np.ndarray):
    return 1/2 * sum(   np.power(sigmoid(data,weights) - patternClass , 2)    )

def summedGradient(data: np.ndarray,weights: np.ndarray, patternClass: np.ndarray):
    gradients = np.zeros(len(weights))
    sigmoidTotal = sigmoid(data,weights)
    for i in range(len(data)):
        sigmoidofI = sigmoid(data[i],weights[i])
        gradients[i] = sum((sigmoidTotal - patternClass)*sigmoidofI*(1-sigmoidofI)*data[i])

    return gradients
# Code For Exercise Three
def exerciseThree(startingData: DataFrame):
    print("Exercise 3")
    data: DataFrame = startingData[['petal_length','petal_width','species']].copy()
    
    # Remove Setosa
    removedAMount: int = len(data[(data['species'] == -1)])
    data.drop(data[(data['species'] == -1)].index, inplace=True)
    data.index = data.index - removedAMount

    xOne = data['petal_length'].to_numpy()
    xZero = np.ones(len(data['petal_length'].to_numpy()))
    xTwo = data['petal_width'].to_numpy() 
    Xs = [xZero, xOne, xTwo]

    
    weights = [-8.4,1.35,1]
    patternClass = data['species'].to_numpy()
    error = meanSquare(Xs,weights,patternClass)
    line1 = multiLine(Xs[0:2],weights[0:2])
    print(error)
    plotXVsY(data,line1,weights[2])

    weights2 = [-2,0.000000000000000000001,0.000000000000000000001]
    print(meanSquare(Xs,weights2,patternClass))
    line2 = multiLine(Xs[0:2],weights2[0:2])

    plotXVsY(data,line2,weights[2])
    
    gradient = summedGradient(Xs,weights,patternClass)
    N=2
    epsilon = 0.1/N
    newWeight=weights-epsilon*gradient
    line3 = multiLine(Xs[0:2],newWeight[0:2])
    print(meanSquare(Xs,weights,patternClass))
    print(meanSquare(Xs,newWeight,patternClass))
    #gradient
    plotXVsY(data,line3,newWeight[2])
    

def exerciseFour(startingData: DataFrame):
    print("Exercise 4")
    data: DataFrame = startingData[['petal_length','petal_width','species']].copy()
    
    # Remove Setosa
    removedAMount: int = len(data[(data['species'] == -1)])
    data.drop(data[(data['species'] == -1)].index, inplace=True)
    data.index = data.index - removedAMount

    # Generate Pattern Vector X
    xOne = data['petal_length'].to_numpy()
    xZero = np.ones(len(data['petal_length'].to_numpy()))
    xTwo = data['petal_width'].to_numpy() 
    Xs: np.ndarray = [xZero, xOne, xTwo]
    
    N = 2
    epsilon =0.01/N
    tol=4.4
    # Bad weights to learn from
    #weights: np.ndarray = [-8.4,1.35,1]
    weights: np.ndarray = [random.uniform(-8,-9),random.uniform(0.9,1.5),random.uniform(0.8,1.2)]
    patternClass = data['species'].to_numpy()
    stepCount = 0

    line: np.ndarray = multiLine(Xs[0:2],weights[0:2])

    Error: list = []

    Error.append(meanSquare(Xs,weights,data['species'].to_numpy()))
    
    plotXVsY(data,line,weights[2])
    errorPlot(Error)
    print(weights)
     
    while(Error[len(Error)-1] > tol):
        stepCount = stepCount +1
        line = multiLine(Xs[0:2],weights[0:2])

        if(stepCount%25==0):
            print(meanSquare(Xs,weights,data['species'].to_numpy()))
            plotXVsY(data,line,weights[2])
            errorPlot(Error)

        weightChange = summedGradient(Xs,weights,patternClass)
        weights = weights -epsilon*weightChange
        Error.append(meanSquare(Xs,weights,data['species'].to_numpy()))

    print("worked")
    plotXVsY(data,line,weights[2])
    errorPlot(Error)

def extraCredit(startingData: DataFrame):
    print("Extra Credit")
    
    data: DataFrame = startingData.copy()

    # followed this tutorial
    # https://medium.com/@nutanbhogendrasharma/tensorflow-deep-learning-model-with-iris-dataset-8ec344c49f91

    # Input Data
    X = data[['sepal_length','sepal_width','petal_length','petal_width']]
    # Species
    y = data['species']

    # Species in 0,1,2
    encoder =  LabelEncoder()
    y1 = encoder.fit_transform(y)
    Y = pd.get_dummies(y1).values
    

    # Trained base on input, class, traing size, seeding
    X_train, X_test, y_train, y_test = train_test_split(X, Y, train_size=0.8, random_state=0)

    # Generate and Compile model
    model = tf.keras.Sequential([
    tf.keras.layers.Dense(10, activation='relu'),
    tf.keras.layers.Dense(10, activation='relu'),
    tf.keras.layers.Dense(3, activation='softmax')
    ])
    
    model.compile(optimizer='rmsprop',
              loss='categorical_crossentropy',
              metrics=['accuracy'])

    # Fit the data based on the model
    model.fit(X_train, y_train, batch_size=50, epochs=100)

    loss, accuracy = model.evaluate(X_test, y_test, verbose=0)
    print('Test loss:', loss)
    print('Test accuracy:', accuracy)

    y_pred = model.predict(X_test)

    actual = np.argmax(y_test,axis=1)
    predicted = np.argmax(y_pred,axis=1)
    print(f"Actual: {actual}")
    print(f"Predicted: {predicted}")


    # Graphs Accuracy Vs. Training Size Vs. X size
    # This take a min or 2
    stepcount: int = 10
    rangeVarData: range = range(1,len(X),stepcount)
    rangeVarX: range = range(1,5)

    allAcc = []

    for j in rangeVarX:
        acc = []

        model_loop = tf.keras.Sequential([
        tf.keras.layers.Dense(10, activation='relu'),
        tf.keras.layers.Dense(10, activation='relu'),
        tf.keras.layers.Dense(3, activation='softmax')
        ])
        model_loop.compile(optimizer='rmsprop',
              loss='categorical_crossentropy',
              metrics=['accuracy'])

        for i in rangeVarData:
            print(i)
            X_train_loop, X_test_loop, y_train_loop, y_test_loop = train_test_split(X[X.columns[0:j]], Y, train_size=i, random_state=0)

            if(X_test_loop.ndim ==1):
                X_test_loop = [X_test_loop]
            

            model_loop.fit(X_train_loop, y_train_loop, batch_size=50, epochs=100,verbose=0)
            loss, accuracy = model_loop.evaluate(X_test_loop, y_test_loop, verbose=0)
            acc.append(accuracy)

        print(acc)
        fig, axis = plot.subplots(figsize=(8, 6))
        plot.plot(rangeVarData, acc,  marker = 'o') 
        
        axis.set_xlabel(r'Training Size', fontsize=20)
        axis.set_ylabel(r'Accuracy', fontsize=20)
        plot.show(block=False)
        plot.ylim((0,1.1))
        plot.pause(10)
        plot.close()

        allAcc.append(acc)
    
    fig = plot.figure(figsize = (8,6))
    ax = plot.axes(projection='3d')
    ax.grid()

    [XMesh,YMesh] = np.meshgrid(rangeVarX,rangeVarData)
    
    ax.plot_surface(XMesh,YMesh,np.transpose(np.array(allAcc)))
    ax.set_title('3D Iris Data Plot')

    # Set axes label
    ax.set_xlabel('Input Data Array', labelpad=20)
    ax.set_ylabel('Training Size', labelpad=20)
    ax.set_zlabel('Accuracy', labelpad=20)
    plot.show(block=False)
    plot.pause(20)
    plot.close()
    
if __name__ == "__main__":
    main()