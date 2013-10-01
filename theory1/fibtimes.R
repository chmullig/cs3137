library(ggplot2)
df <- read.table("fibonacci.cleantimes", header=TRUE)
qplot(data=df, x=n, y=time, color=algorithm, geom="line", xlab="n", ylab="time (seconds)")
ggsave("fibtimes.png", height=3.5, width=6)

library(reshape2)
write.table(dcast(df[df$n %in% c(10, 20, 30, 35, 40, 45, 50, 51),], algorithm ~ n), quote=FALSE, sep=" & ", row.names=FALSE, eol= "\\\\\n\\hline\n")