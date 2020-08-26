/**
 * This module is generated from DebugEndpoint.java
 * All changes to this file are overridden. Please consider to make changes in the corresponding Java file if necessary.
 * @see {@link file://C:\projects\hop-n-pop\server\src\main\java\org\vaadin\erik\game\communication\endpoint\DebugEndpoint.java}
 * @module DebugEndpoint
 */

// @ts-ignore
import client from './connect-client.default';

function _calculateAIPathing(): Promise<void> {
  return client.call('DebugEndpoint', 'calculateAIPathing');
}
export {_calculateAIPathing as calculateAIPathing};

function _despawnAIS(): Promise<void> {
  return client.call('DebugEndpoint', 'despawnAIS');
}
export {_despawnAIS as despawnAIS};

function _setFixedDelta(
  fixedDelta: boolean
): Promise<void> {
  return client.call('DebugEndpoint', 'setFixedDelta', {fixedDelta});
}
export {_setFixedDelta as setFixedDelta};

function _setServerSlowDown(
  slowDownFactor: number
): Promise<void> {
  return client.call('DebugEndpoint', 'setServerSlowDown', {slowDownFactor});
}
export {_setServerSlowDown as setServerSlowDown};

function _spawnAI(): Promise<void> {
  return client.call('DebugEndpoint', 'spawnAI');
}
export {_spawnAI as spawnAI};
