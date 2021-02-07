import axios from "axios";
import Vue from "vue";
import i18n from "@/i18n";

export const httpClient = axios.create({
  timeout: 3000
});

httpClient.interceptors.response.use(
  response => {
    if (response.status === 200 || response.status === 201) {
      return Promise.resolve(response);
    } else {
      return Promise.reject(response);
    }
  },
  error => {
    {
      Vue.$toast.open({
        type: "error",
        message: i18n.tc("api_error"),
        position: "bottom"
      });
    }
  }
);
