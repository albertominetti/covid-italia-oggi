library(ggplot2)
library(scales)

df <- read.csv("https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-regioni/dpc-covid19-ita-regioni.csv")

df["giorno"] <- as.Date(df[, "data"])

lastDay <- format(tail(df$giorno, n = 1), "%Y-%m-%d")
df$stato <- NULL
DATE_BREAKS <- "15 day"

for(r in unique(df$codice_regione) ){
  df_tmp <- df[df$codice_regione == r,]
  df[df$codice_regione == r, "nuovi_deceduti"] <- df_tmp$deceduti - append(values = 0, x = head(df_tmp$deceduti, -1), after = 0)
  df[df$codice_regione == r, "nuovi_tamponi"] <- df_tmp$tamponi - append(values = 0, x = head(df_tmp$tamponi, -1), after = 0)
}

DEST_DIR <- "graphs"
if (!dir.exists(DEST_DIR)) {
  dir.create(DEST_DIR)
}

DAILY_DIR <- paste0(DEST_DIR, "/", lastDay)
if (!dir.exists(DAILY_DIR)) {
  dir.create(DAILY_DIR)
}

df2 <- df[!df$denominazione_regione %in% c("Molise"),] # il Molise non esiste

ggplot(df2, aes(x = giorno, y = nuovi_deceduti, color = denominazione_regione)) +
  geom_line() +
  facet_wrap(~denominazione_regione, scales = "free") +
  theme(axis.text.x = element_text(angle = 90, vjust = 0.25), legend.position = "none") +
  ylab("deceduti") + xlab("") +
  scale_x_date(date_breaks = DATE_BREAKS, date_labels = "%d%b", expand = c(0.01, 1))
ggsave(paste0(DAILY_DIR, "/deceduti_r.png"), width = 18, height = 9)

ggplot(df2, aes(x = giorno, y = nuovi_tamponi, color = denominazione_regione)) +
  geom_line() +
  facet_wrap(~denominazione_regione, scales = "free") +
  theme(axis.text.x = element_text(angle = 90, vjust = 0.25), legend.position = "none") +
  ylab("tamponi") +
  scale_y_continuous(labels = unit_format(unit = "k", scale = 1e-3, sep = "")) +
  scale_x_date(date_breaks = DATE_BREAKS, date_labels = "%d%b", expand = c(0.01, 1))
ggsave(paste0(DAILY_DIR, "/tamponi_r.png"), width = 18, height = 9)

ggplot(df2, aes(x = giorno, y = nuovi_positivi, color = denominazione_regione)) +
  geom_line() +
  facet_wrap(~denominazione_regione, scales = "free") +
  theme(axis.text.x = element_text(angle = 90, vjust = 0.25), legend.position = "none") +
  ylab("positivi") + xlab("") +
  scale_x_date(date_breaks = DATE_BREAKS, date_labels = "%d%b", expand = c(0.01, 1))
ggsave(paste0(DAILY_DIR, "/positivi_r.png"), width = 18, height = 9)

ggplot(df2, aes(x = giorno, y = terapia_intensiva, color = denominazione_regione)) +
  geom_line() + xlab("") +
  facet_wrap(~denominazione_regione, scales = "free") +
  theme(axis.text.x = element_text(angle = 90, vjust = 0.25), legend.position = "none") +
  ylab("terapia intensiva") +
  scale_x_date(date_breaks = DATE_BREAKS, date_labels = "%d%b", expand = c(0.01, 1))
ggsave(paste0(DAILY_DIR, "/terapia_intensiva_r.png"), width = 18, height = 9)

