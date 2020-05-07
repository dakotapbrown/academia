import sys
from typing import List, Tuple
from custom_canvas import CustomCanvas
from rectangle import Rectangle
from rectpack import newPacker


def pack(allRect: List[Rectangle], canvasSize: Tuple[int, int]) -> List[Rectangle]:
    """Pack the rectangles into the specified canvas size using heuristic algorithms
    from the rectpack Pytho module"""

    # List of rectangles that have been placed in the canvas as Rectangle objects
    active_rectangles: List[Rectangle] = []

    # List of rectangles' widths and heights as a tuple
    rectangles: List[Tuple[int, int]] = []
    # List of knapsacks or bins to place the rectangles in
    bin: List[Tuple[int, int]] = [canvasSize]

    packer = newPacker()

    # Convert the Rectangle objects to simple width, height tuples
    for rect in allRect:
        rectangles.append((rect.width, rect.height))

    # Add them all to the packer
    for r in rectangles:
        packer.add_rect(*r)

    # Tell the packer the size of the bin(s) we want the rectangles added to
    for b in bin:
        packer.add_bin(*b)

    packer.pack()

    # Convert the tuples back to Rectangle objects
    for rect in packer.rect_list():
        b, x, y, w, h, rid = rect
        active_rectangles.append(Rectangle(h, w, x, y))

    return active_rectangles


def main() -> None:
    file = open(sys.argv[1], 'r')
    first_line = file.readline()

    canvas_height, canvas_width = first_line.split(',')
    canvas_height = int(canvas_height)
    canvas_width = int(canvas_width)

    rectangles: List[Rectangle] = []

    # Parse each line for rectangle height and width and set x, y to zero
    for line in file:
        height, width = line.split(',')
        rectangles.append(Rectangle(int(height), int(width), 0, 0))

    # Sort the rectangles in descending order based on height for easier manipulation
    rectangles.sort(key=lambda r: r.height, reverse=True)

    # Pack the rectangles into a list of Rectangle objects
    packed_rects = pack(rectangles, (canvas_height, canvas_width))

    # Instantiate our canvas and then add each Rectangle to it
    cv = CustomCanvas(canvas_height, canvas_width)
    for rect in packed_rects:
        cv.add_rectangle(rect.x, rect.y, rect.height, rect.width)

    # Render the canvas and drawings
    cv.render()


class Assignment7:
    if __name__ == "__main__":
        main()
