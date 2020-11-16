library(ggplot2)
library(scales)

data <- read.csv("https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-andamento-nazionale/dpc-covid19-ita-andamento-nazionale.csv")

data["giorno"] <- as.Date(data[, "data"])
lastDay <- format(tail(data$giorno, n = 1), "%Y-%m-%d")
data$stato <- NULL
DATE_BREAKS <- "9 day"

data[, "nuovi_deceduti"] <- data$deceduti - append(values = 0, x = head(data$deceduti, -1), after = 0)
data[, "nuovi_tamponi"] <- data$tamponi - append(values = 0, x = head(data$tamponi, -1), after = 0)
data[, "perc_positivi_su_tamponi"] <- (data$nuovi_positivi / data$nuovi_tamponi)

DEST_DIR <- "graphs"
if (!dir.exists(DEST_DIR)) {
  dir.create(DEST_DIR)
}

DAILY_DIR <- paste0(DEST_DIR, "/", lastDay)
if (!dir.exists(DAILY_DIR)) {
  dir.create(DAILY_DIR)
}

ggplot(data, aes(x = giorno, y = nuovi_deceduti, group = 1)) +
  geom_line() +
  theme(axis.text.x = element_text(angle = 90, vjust = 0.25)) +
  ylab("deceduti") +
  scale_x_date(date_breaks = DATE_BREAKS, date_labels = "%d%b", expand = c(0.01, 1))
ggsave(paste0(DAILY_DIR, "/deceduti.png"), width = 18, height = 9)

ggplot(data, aes(x = giorno, y = nuovi_tamponi, group = 1)) +
  geom_line() +
  theme(axis.text.x = element_text(angle = 90, vjust = 0.25)) +
  ylab("tamponi") +
  xlab("") +
  scale_y_continuous(labels = unit_format(unit = "k", scale = 1e-3, sep = "")) +
  scale_x_date(date_breaks = DATE_BREAKS, date_labels = "%d%b", expand = c(0.01, 1))
ggsave(paste0(DAILY_DIR, "/tamponi.png"), width = 18, height = 9)

ggplot(data, aes(x = giorno, y = perc_positivi_su_tamponi, group = 1)) +
  geom_line() +
  theme(axis.text.x = element_text(angle = 90, vjust = 0.25)) +
  ylab("positivi su tamponi") +
  scale_y_continuous(labels = percent_format(accuracy = 1)) +
  scale_x_date(date_breaks = DATE_BREAKS, date_labels = "%d%b", expand = c(0.01, 1))
ggsave(paste0(DAILY_DIR, "/positivi_su_tamponi.png"), width = 18, height = 9)

ggplot(data, aes(x = giorno, y = nuovi_positivi, group = 1)) +
  geom_line() +
  theme(axis.text.x = element_text(angle = 90, vjust = 0.25)) +
  ylab("positivi") +
  scale_x_date(date_breaks = DATE_BREAKS, date_labels = "%d%b", expand = c(0.01, 1))
ggsave(paste0(DAILY_DIR, "/positivi.png"), width = 18, height = 9)

ggplot(data, aes(x = giorno, y = terapia_intensiva, group = 1)) +
  geom_line() +
  theme(axis.text.x = element_text(angle = 90, vjust = 0.25)) +
  ylab("terapia intensiva") +
  scale_x_date(date_breaks = DATE_BREAKS, date_labels = "%d%b", expand = c(0.01, 1))
ggsave(paste0(DAILY_DIR, "/terapia_intensiva.png"), width = 18, height = 9)
