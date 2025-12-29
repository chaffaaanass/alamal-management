export interface SocietyRequest {
    chequeNumber: number;
    societyName: string;
    date: string;
    sum: number;
    batches: number[];
    batchType: string;
    section: number;
    createdBy: string;
}