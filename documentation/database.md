# GitBoard Database Schema

## Account and User Relations

### account

* id
* display_name
* email
* provider_id
* provider_account_id

### provider
Enumeration of accepted account providers.  Defaults to GOOGLE, FACEBOOK, TWITTER.

* id
* name

## Whiteboard Relations

### whiteboard

* id
* creator
* timestamp
* name
* description

### Whiteboard Actions
All whiteboard actions have the following properties in common.

* id
* whiteboard
* creator
* timestamp
* stroke
* z_index

### path_action
No additional properties, but each path can have one more segments.

### path_action_segment
The different parts of a path.

* path_id
* index
* type
* x
* y

### line_action

* start_x
* start_y
* end_x
* end_y

### ellipse_action

* fill
* x
* y
* height
* width

### rectangle_action

* fill
* x
* y
* height
* width

### text_action

