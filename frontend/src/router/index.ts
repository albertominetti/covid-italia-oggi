import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import Home from "../views/Home.vue";
import VueToast from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";

Vue.use(VueRouter);
Vue.use(VueToast);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue")
  },
  {
    path: "/images",
    name: "Images",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Images.vue")
  },
  {
    path: "/region",
    name: "Region",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Region.vue")
  },
  {
    path: "*",
    name: "",
    redirect: "/"
  }
];

const router = new VueRouter({
  mode: "history",
  routes: routes
});

export default router;
