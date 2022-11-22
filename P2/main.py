import numpy as np
import pandas as pd
from pandas import DataFrame
import matplotlib.pyplot as plot
from matplotlib.colors import ListedColormap


def main():
    
    data: DataFrame = pd.read_csv('P2\irisdata.csv')
    # setosa = 1
    # versicolor = 2
    # virginica = 3
    data: DataFrame = data.replace(to_replace="setosa",value=1)
    data: DataFrame = data.replace(to_replace="versicolor",value=2)
    data: DataFrame = data.replace(to_replace="virginica",value=3)

    #exerciseOne(data)
    exerciseTwo(data)
    

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


def singleLayer(Xs: np.ndarray, weights: np.ndarray):

    y = np.zeros(len(Xs[0]))
    # Sum across weights for all x values
    for i in range(len(weights)):
        for j in range(len(Xs[i])):
            y[j] = y[j] + weights[i]*Xs[i][j]
    
    return y

# Plot line and Data
def plotXVsY(data: DataFrame, y):

    customcmap = ListedColormap(["green","blue"])
    fig, axis = plot.subplots(figsize=(8, 6))
    
    plot.plot(data['petal_length'],y)
    plot.scatter(data['petal_length'], data['petal_width'],  marker = 'o', 
                    c=data['species'].astype('category'), 
                    cmap = customcmap, s=80)

    axis.set_xlabel(r'petal_length', fontsize=20)
    axis.set_ylabel(r'petal_width', fontsize=20)
    plot.show(block=False)
    plot.pause(5)
    plot.close()

# Classifies values of over or under y
def simpleClassify(x: np.array,y: np.array):
    length = len(x)
    z = np.zeros(length)
    for i in range(length):
        z[i] = 1 if (x[i] > y[i]) else 0
    return z

# Code For Exercise Two
def exerciseTwo(startingData: DataFrame):
    print("Exercise 2")
    
    data: DataFrame = startingData[['petal_length','petal_width','species']].copy()
    
    # Remove Setosa
    removedAMount: int = len(data[(data['species'] == 1)])
    data.drop(data[(data['species'] == 1)].index, inplace=True)
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
    bias =2.75
    weights = [bias,-1/5]
    
    xOne = data['petal_length'].to_numpy()
    xZero =np.ones(len(data['petal_length'].to_numpy()))
    Xs = [xZero, xOne]

    line = singleLayer(Xs,weights)
    plotXVsY(data,line)
    
    y = data['petal_width'].to_numpy()
    data['classification'] = simpleClassify(y,line)
    
    fig = plot.figure(figsize = (8,6))
    ax = plot.axes(projection='3d')
    ax.grid()

    data = data.sort_values(by='petal_length',ascending=True)
    data = data.sort_values(by='petal_width',ascending=True)
    data = data.sort_values(by='classification',ascending=True)
    
    ax.plot_trisurf(data['petal_length'],data['petal_width'],data['classification'])
    ax.set_title('3D Iris Data Plot')

    # Set axes label
    ax.set_xlabel('petal_length', labelpad=20)
    ax.set_ylabel('petal_width', labelpad=20)
    ax.set_zlabel('Class', labelpad=20)
    plot.show(block=False)
    plot.pause(5)
    plot.close()

    # Uncomment to see all classifications
    # print(data.to_string())

    # Not Close
    print("Not Close")
    print(data.loc[0],"\n")
    print(data.loc[85],"\n")

    # Correct Close
    print("Correct Close")
    print(data.loc[18],"\n")
    print(data.loc[92],"\n")

    # Incorrect Close
    print("Incorrect Close")
    print(data.loc[20],"\n")
    print(data.loc[84],"\n")
    

def exerciseThree():
    print("Exercise 3")

def exerciseFour():
    print("Exercise 4")

def extraCredit():
    print("Extra Credit")

if __name__ == "__main__":
    main()