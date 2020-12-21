import Vue from "vue";
import VueI18n, { LocaleMessages } from "vue-i18n";
import moment from "moment";

Vue.use(VueI18n);

function getBrowserLocale(options = {}): string | undefined {
  const defaultOptions = { countryCodeOnly: false };
  const opt = { ...defaultOptions, ...options };
  const navigatorLocale =
    navigator.languages !== undefined
      ? navigator.languages[0]
      : navigator.language;
  if (!navigatorLocale) {
    return undefined;
  }
  const trimmedLocale = opt.countryCodeOnly
    ? navigatorLocale.trim().split(/-|_/)[0]
    : navigatorLocale.trim();
  return trimmedLocale;
}

function loadLocaleMessages(): LocaleMessages {
  const locales = require.context(
    "./locales",
    true,
    /[A-Za-z0-9-_,\s]+\.json$/i
  );
  const messages: LocaleMessages = {};
  locales.keys().forEach(key => {
    const matched = key.match(/([A-Za-z0-9-_]+)\./i);
    if (matched && matched.length > 1) {
      const locale = matched[1];
      messages[locale] = locales(key);
    }
  });
  return messages;
}

const supportedLocales = {
  en: "English",
  it: "Italiano",
  pl: "Polski"
};

export function supportedLocalesInclude(locale: string | undefined): boolean {
  if (!locale) {
    return false;
  }
  return Object.keys(supportedLocales).includes(locale);
}

const browserLocale = getBrowserLocale({ countryCodeOnly: true });
console.log("browserLocale: " + browserLocale);

const locale = supportedLocalesInclude(browserLocale)
  ? browserLocale
  : process.env.VUE_APP_I18N_LOCALE || "en";

moment.locale(locale);

export default new VueI18n({
  locale: locale,
  fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || "en",
  messages: loadLocaleMessages()
});
