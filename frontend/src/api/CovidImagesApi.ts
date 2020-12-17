import {httpClient} from "@/mixins/http-commons";
import {CovidImagesModel} from "@/api/model/CovidImages";

export default class CovidImagesApi {
    public static async getImages(): Promise<CovidImagesModel> {
        const response = await httpClient.get<CovidImagesModel>(
            "api/images"
        );
        return response.data;
    }

}
