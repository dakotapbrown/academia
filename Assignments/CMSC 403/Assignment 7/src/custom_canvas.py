from tkinter import Canvas
from tkinter import Tk


class CustomCanvas:

    def __init__(self, height: int, width: int) -> None:
        self.__master = Tk()
        self.__canvas = Canvas(self.__master, height=height, width=width)
        self.__height = height
        self.__width = width

    def add_rectangle(self, x, y, height, width) -> int:
        """Wrapper for Canvas.create_rectangle method"""
        return self.__canvas.create_rectangle(x, y, x + width + 1, y + height + 1, fill='#00ffff')

    def render(self) -> None:
        """Wrapper for Canvas.pack() method"""
        self.__canvas.pack()
        self.__master.mainloop()
