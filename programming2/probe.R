#setwd("~/Documents/Columbia/Current Classes/COMS3137 DSA/programming2/")

require(ggplot2)
#firstProbe <- probe
probe <- rbind(firstProbe, read.csv("probe.txt", skip=1))
probe$hyperparams = paste(probe$beta, probe$alpha)

sort(unlist(tapply(probe$prediction, probe$hyperparams, mean)))


ggplot(probe, aes(x=prediction, color=hyperparams)) + geom_density() + coord_cartesian(ylim=c(0,200))
