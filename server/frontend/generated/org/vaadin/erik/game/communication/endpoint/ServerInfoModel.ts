// @ts-nocheck

import ServerInfo from './ServerInfo';

import {ObjectModel,StringModel,NumberModel,ArrayModel,BooleanModel,Required,ModelType,_getPropertyModel} from '@vaadin/form';

import {Email,Null,NotNull,NotEmpty,NotBlank,AssertTrue,AssertFalse,Negative,NegativeOrZero,Positive,PositiveOrZero,Size,Past,Future,Digits,Min,Max,Pattern,DecimalMin,DecimalMax} from '@vaadin/form';

/**
 * This module is generated from org.vaadin.erik.game.communication.endpoint.ServerInfo.
 * All changes to this file are overridden. Please consider to make changes in the corresponding Java file if necessary.
 */
export default class ServerInfoModel<T extends ServerInfo = ServerInfo> extends ObjectModel<T> { 
  static createEmptyValue: () => ServerInfo;

  get debugEnabled(): BooleanModel {
    return this[_getPropertyModel]('debugEnabled', BooleanModel, [false]);
  }

  get full(): BooleanModel {
    return this[_getPropertyModel]('full', BooleanModel, [false]);
  }
}
