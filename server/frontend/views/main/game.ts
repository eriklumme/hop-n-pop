import Communicator from "./communicator";

export default class Game {

    private ctx: CanvasRenderingContext2D;

    private readonly communicator: Communicator;

    private readonly width: number;
    private readonly height: number;

    constructor(canvas: HTMLCanvasElement) {
        this.ctx = <CanvasRenderingContext2D>canvas.getContext("2d");
        this.width = canvas.width;
        this.height = canvas.height;
        this.communicator = new Communicator(this);
    }

    private clear() {
        this.ctx.clearRect(0, 0, this.width, this.height);
    }

    draw(x: number, y: number) {
        this.clear();
        this.ctx.fillRect(x, y, 20, 20);
    }

    move(direction: string) {
        this.communicator.sendPlayerCommand(direction);
    }
}