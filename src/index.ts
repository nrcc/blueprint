import { registerPlugin } from '@capacitor/core';

import type { NrccBluePrintPlugin } from './definitions';

const NrccBluePrint = registerPlugin<NrccBluePrintPlugin>('NrccBluePrint', {
  web: () => import('./web').then(m => new m.NrccBluePrintWeb()),
});

export * from './definitions';
export { NrccBluePrint };
