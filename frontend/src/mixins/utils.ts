import moment from "moment";

export function displayDate(date: number | Date) {
  return moment(date).format("dddd DD MMMM");
}
