import i18n from "@/i18n";
import VueApexCharts from 'vue-apexcharts/dist/vue-apexcharts';

function loadApexLocale(locale: string): any {
    const locales = require.context('apexcharts/dist/locales/', false, /[A-Za-z0-9-]+\.json$/i);
    for (const key of locales.keys()) {
        const matched = key.match(/([A-Za-z0-9-]+)\./i)
        if (matched && matched.length > 1 && locale === matched[1]) {
            console.log("Found!")
            return locales(key)
        }
    }
    return null;
}

const apexLocale = loadApexLocale(i18n.locale);

export const apexLocales = [apexLocale];
export const apexDefaultLocale = apexLocale ? i18n.locale : 'en';

export default VueApexCharts;
