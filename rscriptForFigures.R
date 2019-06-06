library(tidyverse)
setwd("/home/jan/Desktop/Computer Science/projectDA3018")

degAfter <- read.csv("degreesAfter.txt", header = FALSE)

degPrior <- read.csv("degreesPrior.txt", header = FALSE)

degPrior %>% 
  data.frame() %>% 
  setNames(c("val")) %>% 
  ggplot(aes(val)) +
    geom_bar(binwidth = 1) +
    xlab("Degree") +
    ylab("Count") +
    scale_x_continuous(lim = c(0,50))

degAfter %>% 
  data.frame() %>% 
  setNames(c("val")) %>% 
  ggplot(aes(val)) +
  geom_bar(binwidth = 1) +
  xlab("Degree") +
  ylab("Count") +
  scale_x_continuous(lim = c(0,50))

sizesPrior <- read.csv("sizesPrior.txt", header = FALSE)

sizesAfter <- read.csv("sizesAfter.txt", header = FALSE)

sizesPrior %>% 
  data.frame() %>%
  setNames(c("val")) %>% 
  filter(val < 100) %>% 
  ggplot(aes(val)) +
    geom_bar(binwidth = 1) +
    xlab("Size") +
    ylab("Count")


sizesAfter %>% 
  data.frame() %>%
  setNames(c("val")) %>% 
  filter(val < 100) %>% 
  ggplot(aes(val)) +
  geom_bar(binwidth = 1) +
  xlab("Size") +
  ylab("Count")









before_data <- read.csv("degreebefore", header = FALSE)
before <- data.frame(before_data)

after_data <- read.csv("degreeafter", header = FALSE)
after <- data.frame(after_data)

after_n <- after %>%
  data.frame() %>% 
  group_by(X0) %>%
  summarise(n=n()) %>% 
  setNames(c("val", "after"))

before_n <- before %>%
  data.frame() %>% 
  group_by(X30) %>%
  summarise(n=n()) %>% 
  setNames(c("val", "before"))


both <- full_join(after_n, before_n, by="val")

both[is.na(both)]=0

both %>%
  gather(when, n, after:before)%>%
  ggplot(aes(x=val, y=n, color=when))+geom_point()+geom_line(size=1)+scale_x_continuous(lim=c(0,50))+
  labs(title = "Degree distribution before and after partitioning", x="Degree", y="")