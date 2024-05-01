export type BluetoothDevice = { name: string, address: string, type: BluetoothDeviceType };

export interface NrccBluePrintPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  list(): Promise<{ devices: BluetoothDevice[] }>;
  connect(options: { address: string }): Promise<void>;
  print(options: { address: string, data: string, printType: string }): Promise<void>;
  disconnect(): Promise<void>;

  connectAndPrint(options: { address: string, data: string, printType: string }): Promise<void>;
}


export enum BluetoothDeviceType {
  Unknown = "unknown",
  Classic = "classic",
  Le = "le",
  Dual = "dual",
}
