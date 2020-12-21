export interface CovidDataModel {
  lastDate: Date;
  startDate: Date;
  inIntensiveCare: number[];
  newDeceased: number[];
  newPositives: number[];
  newTests: number[];
}

export class CovidDataDTO implements CovidDataModel {
  lastDate = new Date();
  startDate = new Date();
  inIntensiveCare: number[] = [];
  newDeceased: number[] = [];
  newPositives: number[] = [];
  newTests: number[] = [];
}

export default class CovidData extends CovidDataDTO {
  constructor(dto: CovidDataDTO) {
    super();
    Object.assign(this, dto);
  }

  public get inIntensiveCareTs(): Point[] {
    return CovidData.prepareTimeSeries(this.startDate, this.inIntensiveCare);
  }

  public get newDeceasedTs(): Point[] {
    return CovidData.prepareTimeSeries(this.startDate, this.newDeceased);
  }

  public get newPositivesTs(): Point[] {
    return CovidData.prepareTimeSeries(this.startDate, this.newPositives);
  }

  public get newTestsTs(): Point[] {
    return CovidData.prepareTimeSeries(this.startDate, this.newTests);
  }

  private static prepareTimeSeries(
    firstDate: Date,
    rawSeries: number[]
  ): Point[] {
    const series: Point[] = [];
    const currDate = new Date(firstDate);
    for (const e of rawSeries) {
      series.push({
        x: currDate.getTime(),
        y: e
      });
      currDate.setDate(currDate.getDate() + 1);
    }
    return series;
  }
}

export interface Point {
  x: number;
  y: number;
}

/// CovidData.ts
