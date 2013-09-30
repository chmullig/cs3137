library(ggplot2)
df <- read.table("fibonacci.cleantimes")
qplot(data=df, x=n, y=time, color=type, geom="line", xlab="n", ylab="time (seconds)")
ggsave("fibtimes.png", height=3.5, width=6)