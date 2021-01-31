import React from 'react';

import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

const useStyles = makeStyles({
  root: {
    maxWidth: 300,
  },
});

export default function Ad({id, make, model, description, category, price, customer, createTime, updateTime, ...props}) {
  const classes = useStyles();

  const dateTime = new Date(createTime).toDateString();

  return (
    <Card className={classes.root}>
      <CardActionArea>
        <CardMedia
          component="img"
          alt="Sample Vehicle"
          height="140"
          image="https://picsum.photos/300" // this just loads a random 300x300 image, to make the ad in this demo more interesting :)
          title={description}
        />
        <CardContent>
          <Typography gutterBottom variant="h5" component="h2">
            {category} - {make} - {model}
          </Typography>
          <Typography variant="body2" color="textSecondary" component="p">
            €{price}
          </Typography>
          <Typography variant="body2" color="textSecondary" component="p" noWrap="true">
            {description}
          </Typography>
          <Typography variant="body2" color="textSecondary" component="p">
            Submitted at: {dateTime}
          </Typography>
          <Typography variant="body2" color="textSecondary" component="p">
            Seller: {customer.firstName} {customer.lastName}
          </Typography>
          <Typography variant="body2" color="textSecondary" component="p">
            Email: {customer.email}
          </Typography>
        </CardContent>
      </CardActionArea>
      <CardActions>
        <Button size="small" color="primary">
          Learn More
        </Button>
      </CardActions>
    </Card>
  );
}
