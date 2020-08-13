/**
 * This module is generated from DebugEndpoint.java
 * All changes to this file are overridden. Please consider to make changes in the corresponding Java file if necessary.
 * @see {@link file://C:\projects\hop-n-pop\server\src\main\java\org\vaadin\erik\game\communication\endpoint\DebugEndpoint.java}
 * @module DebugEndpoint
 */

// @ts-ignore
import client from './connect-client.default';

function _setServerSlowDown(
  slowDownFactor: number
): Promise<void> {
  return client.call('DebugEndpoint', 'setServerSlowDown', {slowDownFactor});
}
export {_setServerSlowDown as setServerSlowDown};
