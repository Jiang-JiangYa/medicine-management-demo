import http from "@/api";
export const uploadImg = async (params: FormData) => {
  const response = await http.post<string>("/file/upload", params, { cancel: false });
  return { ...response, data: { fileUrl: response.data } };
};
export const uploadVideo = (_params: FormData): Promise<{ data: { fileUrl: string } }> =>
  Promise.reject(new Error("演示环境不支持视频上传"));
