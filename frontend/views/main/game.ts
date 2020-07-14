export default class Game {

    private ctx: CanvasRenderingContext2D;

    private readonly width: number;
    private readonly height: number;

    constructor(canvas: HTMLCanvasElement) {
        this.ctx = <CanvasRenderingContext2D>canvas.getContext("2d");
        this.width = canvas.width;
        this.height = canvas.height;
    }

    private clear() {
        this.ctx.clearRect(0, 0, this.width, this.height);
    }

    draw(x: number, y: number) {
        this.clear();
        this.ctx.fillRect(x, y, 20, 20);
    }
}