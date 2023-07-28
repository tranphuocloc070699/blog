import { FormData } from "node-fetch-native";
import { IMultipartFormData } from "~/static/types/other";

export async function convertRequestToFile(
  requestBody: any
): Promise<File | string> {
  return new Promise<File | string>((resolve, reject) => {
    const { filename, type, data } = requestBody;
    try {
      const file = new File([data], filename, { type });
      resolve(file);
    } catch (error) {
      reject(error);
    }
  });
}

export function convertedSentence(sentence: string) {
  return sentence
    .toLowerCase()
    .replace(/\s+/g, "-")
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "");
}

export function convertObjectToFormData(reactiveData: any): FormData {
  const formData = new FormData();
  for (const key in reactiveData) {
    const value = reactiveData[key];

    if (value instanceof File) {
      formData.append(key, value);
    } else if (value === null || typeof value === "object") {
      const jsonValue = JSON.stringify(value);
      const blob = new Blob([jsonValue], { type: "application/json" });
      formData.append(key, blob);
    } else {
      formData.append(key, value.toString());
    }
  }
  return formData;
}

export async function convertMultipartToFormData(data: any): Promise<FormData> {
  const formData = new FormData();

  for (const item of data) {
    switch (true) {
      case item.type === "application/json":
        formData.append(item.name, item.data.toString("utf-8"));
        break;
      case item.type?.includes("image"):
        const response = await convertRequestToFile(item);
        if (response instanceof File) {
          formData.append(item.name, response);
        } else {
          console.log('[convertMultipartToFormData] image case error',response)
        }
        break;
      default:
        formData.append(item.name, item.data.toString("utf-8"));
        break;
    }
  }

  return formData;
}

export function formatDate(dateString : string) {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${day}-${month}-${year}`;
}