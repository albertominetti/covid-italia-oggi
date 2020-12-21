import { httpClient } from "@/mixins/http-commons";

import CovidData, { CovidDataModel } from "@/api/model/CovidData";

export default class CovidDataApi {
  public static async getNationalCovidData(): Promise<CovidData> {
    const response = await httpClient.get<CovidDataModel>("/info");
    return new CovidData(response.data);
  }

  public static async getRegionalCovidData(
    regionCode: string
  ): Promise<CovidData> {
    const response = await httpClient.get<CovidDataModel>(
      "/api/data/regions/" + regionCode
    );
    return new CovidData(response.data);
  }
}
