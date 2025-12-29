import { SocietyResponse } from "./society-response";

export interface SocietyGroup {
  rows: SocietyResponse[];
  batches: number[];
}