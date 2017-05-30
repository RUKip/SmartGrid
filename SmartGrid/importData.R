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

addProductionPlot <- function(productionData) {
  points(productionData$DateTime, productionData$amount, col="red")
  lines(productionData$DateTime, productionData$amount, col="red")
}

addEnergyStatus <- function(energyStatusData) {
  points(energyStatusData$DateTime, energyStatusData$amount, col="blue")
  lines(energyStatusData$DateTime, energyStatusData$amount, col="blue")
}

library("chron")
setwd("/media/HDD-Thijs/Schooldocumenten/2016-2017/BachalorProject/bachelorproject/bachelorproject/SmartGrid/output")

#plot data
for (folder in list.dirs(recursive = FALSE, full.names = FALSE)) {
  print(paste0(folder,"/production.csv"))
  production <- readCSV(paste0(folder,"/production.csv"))
  energyStatus <- readCSV(paste0(folder,"/energyStatus.csv"))
  pdf(paste0(folder,".pdf"))
  plot(energyStatus$DateTime, energyStatus$amount, main = folder, type = 'n', xlab="Time",ylab="Energy (Joule)")
  axis(1, at=c(0,10000000), labels=c("",""), lwd.ticks=0)
  addProductionPlot(production)
  addEnergyStatus(energyStatus)
  #plot(production$DateTime, production$amount, main=folder)
  #lines(production$DateTime, production$amount)
  dev.off()
}