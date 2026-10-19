import { defineStore } from "pinia";
import { UserState } from "@/stores/interface";
import piniaPersistConfig from "@/stores/helper/persist";

export const useUserStore = defineStore({
  id: "HIS-user",
  state: (): UserState => ({
    token: "",
    userInfo: { username: "Geeker", id: "", name: "" }
  }),
  getters: {},
  actions: {
    // Set Token
    setToken(token: string) {
      this.token = token;
    },
    // Set setUserInfo
    setUserInfo(userInfo: UserState["userInfo"]) {
      this.userInfo.username = userInfo.username;
      this.userInfo.id = userInfo.id;
      this.userInfo.name = userInfo.name;
    },
    getUserInfo() {
      return this.userInfo;
    }
  },
  persist: piniaPersistConfig("HIS-user")
});
