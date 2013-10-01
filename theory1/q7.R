require(ggplot2)
ns <- (0:100000)/10
main <- ggplot(NULL, aes(x=x, color=name)) + 
    stat_function(data=data.frame(x=ns, name="a) 10n^2"), fun=function(n) 10*n^2) +  
    stat_function(data=data.frame(x=ns, name="b) log_3(n)"), fun=function(n) log(n, base=3)) +
    stat_function(data=data.frame(x=ns, name="c) 2^n"), fun=function(n) 2^n) +
    stat_function(data=data.frame(x=ns, name="d) 10n"), fun=function(n) 10*n) +
    stat_function(data=data.frame(x=ns, name="e) 8"), fun=function(n) 8) +
    stat_function(data=data.frame(x=ns, name="f) log_2(n)"), fun=function(n) log2(n)) +
    stat_function(data=data.frame(x=ns, name="g) nlog_2(n)"), fun=function(n) n*log2(n)) +
    stat_function(data=data.frame(x=ns, name="h) n!"), fun=function(n) gamma(n+1)) +
    ylab("f(n)") + xlab("n")

main + coord_cartesian(y=c(0, 25))
main + coord_cartesian(x=c(0, 500), y=c(0, 1000))
main + coord_cartesian(x=c(0, 20), y=c(0, 2000))
main + coord_cartesian(x=c(0, 20), y=c(0, 10000))
main + coord_cartesian(x=c(0, 2500), y=c(0, 20000))