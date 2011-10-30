#!/bin/bash
sed 's/<ui version="4.0">/<ui version="4.0" language="jambi">/g' $1 | tail -n +2
