Tests for **jrosactionlib** library.

Some integration tests rely on `test_server` being running. To run it you may need to build it first:

``` bash
sudo apt install ros-noetic-catkin
sudo apt install ros-noetic-actionlib-tutorials
cd ws
catkin_make
source devel/setup.zsh
```

Once it is built you can start it with:

``` bash
rosrun test_server test_server_node
```
