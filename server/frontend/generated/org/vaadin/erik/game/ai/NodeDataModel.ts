import StepDataModel from './StepDataModel';
import NodeData from './NodeData';

// @ts-ignore
import {ObjectModel,StringModel,NumberModel,ArrayModel,BooleanModel,Required} from '@vaadin/form';

// @ts-ignore
import {Email,Null,NotNull,NotEmpty,NotBlank,AssertTrue,AssertFalse,Negative,NegativeOrZero,Positive,PositiveOrZero,Size,Past,PastOrPresent,Future,FutureOrPresent,Digits,Min,Max,Pattern,DecimalMin,DecimalMax} from '@vaadin/form';

/**
 * This module is generated from org.vaadin.erik.game.ai.NodeData.
 * All changes to this file are overridden. Please consider to make changes in the corresponding Java file if necessary.
 */
export default class NodeDataModel<T extends NodeData = NodeData> extends ObjectModel<T> { 
  static createEmptyValue: () => NodeData;
  public readonly stepData = new ArrayModel(this, 'stepData', StepDataModel);
  public readonly x = new NumberModel(this, 'x');
  public readonly y = new NumberModel(this, 'y');
}
