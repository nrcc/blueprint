export interface NrccBluePrintPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
