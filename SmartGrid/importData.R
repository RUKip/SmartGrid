library("chron")
csv <- read.csv("output.csv")
csv$Date <- as.chron(dates = csv$Date, times = csv$Time, format=c("m/d/y","h:m:s"))
#csv$Date <- as.Date(csv$Date)
csv$Date <- sapply(csv$Date, as.character)
a <- chron(dates = csv$Date, times = csv$Time, format=c("d/m/y","h:m:s"))
plot(a,csv$amountEnergy,type="n")
lines(a, csv$amountEnergy)

production <- read.csv("production.csv")
production$Date <- as.chron(dates = production$Date, times = production$Time, format=c("m/d/y","h:m:s"))
#csv$Date <- as.Date(csv$Date)
production$Date <- sapply(production$Date, as.character)
b <- chron(dates = production$Date, times = production$Time, format=c("d/m/y","h:m:s"))

setCorrectDate <- function(dataSet) {
  #add the dataTime colom
  dataSet$Date <- sapply(dataSet$Date, as.character)
  dataSet$DateTime <- chron(dates = dataSet$Date, times = dataSet$Time, format=c("d/m/y","h:m:s")) 
  
  #remove the date and time colum
  dataSet$Date <- NULL
  dataSet$Time <- NULL
  
  return(dataSet)
}