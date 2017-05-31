setCorrectDate <- function(dataSet) {
  #add the dataTime colom
  dataSet$Date <- sapply(dataSet$Date, as.character)
  dataSet$DateTime <- chron(dates = dataSet$Date, times = dataSet$Time, format=c("d/m/y","h:m:s")) 
  
  #remove the date and time colum
  dataSet$Date <- NULL
  dataSet$Time <- NULL
  
  return(dataSet)
}

readCSV <- function(filename) {
  csv <- read.csv(filename)
  correctDate <- setCorrectDate(csv)
  return(correctDate)
}

initializeMinMax <- function() {
  minAmount <<- 0
  maxAmount <<- 0
  minTime <<- NULL
  maxTime <<- NULL
}

updateMinMax <- function(dataset) {
  for (curAmount in dataset$amount) {
    if (curAmount < minAmount) {
      minAmount <<- curAmount
    } else if (curAmount > maxAmount) {
      maxAmount <<- curAmount
    }
  }
  for (curTime in dataset$DateTime){
    if (is.null(minTime)) {
      minTime <<- curTime
    } else if (curTime < minTime) {
      minTime <<- curTime
    } 
    
    if (is.null(maxTime)) {
      maxTime <<- curTime
    } else if (curTime > maxTime) {
      maxTime <<- curTime
    }
  }
}

addProductionPlot <- function(productionData) {
  points(productionData$DateTime, productionData$amount, col="yellow")
  lines(productionData$DateTime, productionData$amount, col="yellow")
}

addEnergyStatus <- function(energyStatusData) {
  points(energyStatusData$DateTime, energyStatusData$amount, col="blue")
  lines(energyStatusData$DateTime, energyStatusData$amount, col="blue")
}

addSellerDeals <- function(sellerDealsData) {
  points(sellerDealsData$DateTime, sellerDealsData$amount, col="green")
  lines(sellerDealsData$DateTime, sellerDealsData$amount, col="green")
}

addBuyerDeals <- function(buyerDealsData) {
  points(buyerDealsData$DateTime, buyerDealsData$amount, col="red")
  lines(buyerDealsData$DateTime, buyerDealsData$amount, col="red")
}

setPlotDimensions <- function() {
  #read data
  initializeMinMax()
  for (folder in list.dirs(recursive = FALSE, full.names = FALSE)) {
    #TODO: find a way to store the data.
    production <- readCSV(paste0(folder,"/production.csv"))
    energyStatus <- readCSV(paste0(folder,"/energyStatus.csv"))
    sellerDeals <- readCSV(paste0(folder,"/sellerDeals.csv"))
    buyerDeals <- readCSV(paste0(folder,"/buyerDeals.csv"))
    updateMinMax(production)
    updateMinMax(energyStatus)
    updateMinMax(sellerDeals)
    updateMinMax(buyerDeals)
  }
}

#plot data
plotData <- function() {
  setPlotDimensions()
  for (folder in list.dirs(recursive = FALSE, full.names = FALSE)) {
    print(paste0(folder))
    production <- readCSV(paste0(folder,"/production.csv"))
    energyStatus <- readCSV(paste0(folder,"/energyStatus.csv"))
    sellerDeals <- readCSV(paste0(folder,"/sellerDeals.csv"))
    buyerDeals <- readCSV(paste0(folder,"/buyerDeals.csv"))
    pdf(paste0(folder,".pdf"))
    plot(c(minTime,maxTime), c(minAmount,maxAmount), main = folder, type = 'n', xlab="Time",ylab="Energy (Joule)")
    addProductionPlot(production)
    addEnergyStatus(energyStatus)
    addSellerDeals(sellerDeals)
    addBuyerDeals(buyerDeals)
    dev.off()
  }
}

start <- function() {
  library("chron")
  setwd("/media/HDD-Thijs/Schooldocumenten/2016-2017/BachalorProject/bachelorproject/bachelorproject/SmartGrid/output")
  plotData()
}