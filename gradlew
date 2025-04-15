#!/bin/sh
exec java -Xmx64m -Xms64m -classpath "$0.jar" org.gradle.wrapper.GradleWrapperMain "$@"

