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

/**
 * Records the movement between two nodes, such that the AI can use it.    Starts recording when close enough to an existing node,  and stops recording when first coming enough to any other node.
 *
 *
 */
function _recordMovementForAI(): Promise<void> {
  return client.call('DebugEndpoint', 'recordMovementForAI');
}
export {_recordMovementForAI as recordMovementForAI};

function _saveRecordedData(): Promise<void> {
  return client.call('DebugEndpoint', 'saveRecordedData');
}
export {_saveRecordedData as saveRecordedData};

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
