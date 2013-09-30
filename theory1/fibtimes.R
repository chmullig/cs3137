library(ggplot2)
df <- read.table("fibonacci.cleantimes", header=TRUE)
qplot(data=df, x=n, y=time, color=algorithm, geom="line", xlab="n", ylab="time (seconds)")
ggsave("fibtimes.png", height=3.5, width=6)

library(reshape2)
print(dcast(df, n ~ algorithm))