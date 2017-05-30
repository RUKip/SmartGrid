library("chron")

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

findMinMax <- function(filename) {
  
}

addProductionPlot <- function(productionData) {
  points(productionData$DateTime, productionData$amount, col="red")
  lines(productionData$DateTime, productionData$amount, col="red")
}

addEnergyStatus <- function(energyStatusData) {
  points(energyStatusData$DateTime, energyStatusData$amount, col="blue")
  lines(energyStatusData$DateTime, energyStatusData$amount, col="blue")
}

addSellerDeals <- function(sellerDealsData) {
  points(sellerDealsData$DateTime, sellerDealsData$amount, col="green")
  lines(sellerDealsData$DateTime, sellerDealsData$amount, col="green")
}

library("chron")
setwd("/media/HDD-Thijs/Schooldocumenten/2016-2017/BachalorProject/bachelorproject/bachelorproject/SmartGrid/output")
minAmount <- 0
maxAmount <- 0
minTime <- 0
maxTime <- 0

#read data
dataHolder <- data.frame(row.names = list.dirs(recursive = FALSE, full.names = FALSE))
for (folder in list.dirs(recursive = FALSE, full.names = FALSE)) {
  print(paste0(folder))
  dataHolder$production <- readCSV(paste0(folder,"/production.csv"))
  dataHolder$energyStatus <- readCSV(paste0(folder,"/energyStatus.csv"))
  dataHolder$sellerDeals <- readCSV(paste0(folder,"/sellerDeals.csv"))
}
#plot data
for (folder in list.dirs(recursive = FALSE, full.names = FALSE)) {
  print(paste0(folder))
  production <- readCSV(paste0(folder,"/production.csv"))
  energyStatus <- readCSV(paste0(folder,"/energyStatus.csv"))
  sellerDeals <- readCSV(paste0(folder,"/sellerDeals.csv"))
  pdf(paste0(folder,".pdf"))
  plot(c(-90,90), c(-8,8), main = folder, type = 'n', xlab="Time",ylab="Energy (Joule)")
  addProductionPlot(production)
  addEnergyStatus(energyStatus)
  addSellerDeals(sellerDeals)
  #plot(production$DateTime, production$amount, main=folder)
  #lines(production$DateTime, production$amount)
  dev.off()
  print(paste(minY, maxY, minX, maxX))
}