import { WebPlugin } from '@capacitor/core';

import type { NrccBluePrintPlugin } from './definitions';

export class NrccBluePrintWeb extends WebPlugin implements NrccBluePrintPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
