import { WebPlugin } from '@capacitor/core';

import type { BluetoothDevice, NrccBluePrintPlugin } from './definitions';

export class NrccBluePrintWeb extends WebPlugin implements NrccBluePrintPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  connect(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  connectAndPrint(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  disconnect(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  list(): Promise<{ devices: BluetoothDevice[] }> {
    throw this.unimplemented('Not implemented on web.');
  }

  print(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }
  
}
